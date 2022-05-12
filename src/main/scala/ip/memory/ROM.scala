package ip.memory

import chisel3._
import ip.memory._
import chisel3.util.MuxCase

// TODO: runレジスタのドメインが立ち下がりエッジになっている
// モジュール全体のレジスタのドメインを立ち上がりエッジに統一する
class ROM(
    stCtlAddr: Int,
    addrAddr: Int,
    inAddr: Int,
    file: String,
    words: Int
) extends Module {
  val io = IO(new Bundle {
    /* Read Only Memory for instructions */
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    val out = Output(UInt(16.W))

    /* instruction memory */
    val pc = Input(UInt(16.W))
    val outInst = Output(UInt(16.W))
    val run = Output(Bool())
  })

  /* ROM */
  //  15       12           8  7       4             0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //                                 W S            S
  //                                 R P            P
  //                                 I R            R
  //                                 T O            O
  //                                 E M            M

  // addr register
  //  15       12           8  7       4            0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|

  // in register
  //  15       12           8  7       4            0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|

  // status and control register
  val romStCtlReg = RegInit(
    VecInit(Seq.fill(16)(false.B))
  )

  // address register
  val addrReg = RegInit(0.asUInt)

  // in register
  val inReg = RegInit(0.asUInt)

  // EBROM and SPRAM
  val ebrom = Module(new EBROM(file, words))
  // val spram = Module(new SPRAM())
  val spram = Module(new SPRAMStub())

  // inner register
  val run = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    RegInit(false.B)
  }
  io.run := romStCtlReg(4)

  /* status register */
  romStCtlReg(0) := run

  /* control register */
  // switch instruction memory from EBROM to SPRAM
  // negative edge
  when(io.addrM === stCtlAddr.asUInt && io.writeM) {
    run := io.inM(4)
  }

  // positive edge
  when(io.addrM === stCtlAddr.asUInt && io.writeM) {
    romStCtlReg(4) := io.inM(4)
    romStCtlReg(5) := io.inM(5)
  }.otherwise {
    romStCtlReg(4) := false.B
    romStCtlReg(5) := false.B
  }

  /* adress register */
  when(io.addrM === addrAddr.asUInt && io.writeM) {
    addrReg := io.inM
  }

  /* in register */
  when(io.addrM === inAddr.asUInt && io.writeM) {
    inReg := io.inM
  }

  /* connect IO */
  io.out := MuxCase(
    0.asUInt,
    Seq(
      (io.addrM === stCtlAddr.asUInt) -> romStCtlReg.asUInt,
      (io.addrM === addrAddr.asUInt) -> addrReg,
      (io.addrM === inAddr.asUInt) -> inReg
    )
  )

  ebrom.io.addr := Mux(run, 0.asUInt, io.pc)

  spram.io.in := inReg
  spram.io.addr := Mux(run, io.pc, addrReg)
  spram.io.writeM := romStCtlReg(5)

  io.outInst := Mux(run, spram.io.out, ebrom.io.out)
}
