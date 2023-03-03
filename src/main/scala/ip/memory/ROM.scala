package ip.memory

import chisel3._
import ip.memory._
import chisel3.util.MuxCase
import chisel3.experimental.Analog
import chisel3.experimental.attach
import os.write

// TODO: runレジスタのドメインが立ち下がりエッジになっている
// モジュール全体のレジスタのドメインを立ち上がりエッジに統一する
class ROM(
    stCtlAddr: Int,
    addrAddr: Int,
    inAddr: Int,
    file: String,
    words: Int,
    doTest: Boolean
) extends Module {
  val io = IO(new Bundle {
    /* Read Only Memory for instructions */
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))
    val outM = Output(UInt(16.W))

    /* instruction memory */
    val pc = Input(UInt(16.W))
    val outInst = Output(UInt(16.W))
    val run = Output(Bool())

    /* SROM I/O */
    val SRAM_DATA = Analog(16.W)
    val SRAM_ADDR = Output(UInt(18.W))
    val SRAM_CSX = Output(Bool())
    val SRAM_OEX = Output(Bool())
    val SRAM_WEX = Output(Bool())
  })

  /* ROM */
  //  15       12           8  7       4             0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //                                 W S            E
  //                                 R R            B
  //                                 I O            R
  //                                 T M            O
  //                                 E              M

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

  // inner register
  val run = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    RegInit(false.B)
  }
  io.run := romStCtlReg(4)

  /* status register */
  romStCtlReg(0) := run

  /* control register */
  // switch instruction memory from EBROM to SROM
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
  io.outM := MuxCase(
    0.asUInt,
    Seq(
      (io.addrM === stCtlAddr.asUInt) -> romStCtlReg.asUInt,
      (io.addrM === addrAddr.asUInt) -> addrReg,
      (io.addrM === inAddr.asUInt) -> inReg
    )
  )

  /*  */
  val ebrom = Module(new EBROM(file, words))
  ebrom.io.addrM := io.pc
  if (doTest) {
    val rom_mock = Module(new EBRAM(file, 1024 * 10 /* 10 KB*/ ))
    rom_mock.io.inM := inReg
    rom_mock.io.writeM := romStCtlReg(5)
    rom_mock.io.addrM := Mux(run, io.pc, addrReg)

    /* SROM I/O */
    io.SRAM_CSX := true.B // inactive
    io.SRAM_OEX := true.B // inactive
    io.SRAM_WEX := true.B // inactive

    io.outInst := Mux(run, rom_mock.io.outM, ebrom.io.outM)
  } else {
    val srom = Module(new BlackBoxSROM())
    srom.io.clock := clock
    srom.io.inM := inReg
    srom.io.writeM := romStCtlReg(5)
    srom.io.addrM := Mux(run, io.pc, addrReg)

    /* SROM I/O */
    attach(srom.io.SRAM_DATA, io.SRAM_DATA)
    io.SRAM_ADDR := srom.io.SRAM_ADDR
    io.SRAM_CSX := srom.io.SRAM_CSX
    io.SRAM_OEX := srom.io.SRAM_OEX
    io.SRAM_WEX := srom.io.SRAM_WEX
    io.outInst := Mux(run, srom.io.outM, ebrom.io.outM)
  }

}
