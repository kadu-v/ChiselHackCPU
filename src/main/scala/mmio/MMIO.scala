package mmio

import chisel3._
import memory.RAM
import uart.{Rx, Tx}
import chisel3.util.MuxCase
import javax.swing.InputMap

class MMIO(init: String) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    // UART
    // Rx
    val rx = Input(Bool())
    val rts = Output(Bool())

    // Tx
    val cts = Input(Bool())
    val tx = Output(Bool())

    // Output to core
    val out = Output(UInt(16.W))

    // Debug signal
    val debug = Output(UInt(16.W))
    val rxdebug = Output(UInt(16.W))
  })

  /* Random Access Memory */
  val ram = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    Module(new RAM(init))
  }
  ram.io.addr := io.addrM
  ram.io.in := io.inM
  ram.io.writeM := Mux(
    io.writeM && io.addrM(13) === 0.U,
    true.B,
    false.B
  )

  /* UART */
  //  15       12  Tx       8  7       4   Rx        0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //            R          B         C           R B
  //            U          U         L           E U
  //            N          S         E           C S
  //                       Y         A           I Y
  //                                 R           E
  //                                             V
  //                                             E

  // staus and controll register
  val stCtlReg = RegInit(
    VecInit(Seq.fill(16)(false.B))
  )

  //  RX
  val rx = Module(new Rx(12, 115200))
  rx.io.rx := io.rx
  io.rts := rx.io.rts
  rx.io.cbf := stCtlReg(5)

  // Tx
  // buffer for Tx
  val txBuff = RegInit(0.asUInt())
  val tx = Module(new Tx(12, 115200))
  tx.io.din := txBuff
  tx.io.run := stCtlReg(12)
  tx.io.cts := io.cts
  io.tx := tx.io.tx

  // status register
  // Rx
  stCtlReg(0) := rx.io.busy // busy flag
  stCtlReg(1) := rx.io.recieved // recieved flag
  // Tx
  stCtlReg(8) := tx.io.busy

  // controll register
  when(io.addrM === 8192.asUInt() && io.writeM) {
    // Rx
    stCtlReg(5) := io.inM(5)
    // Tx
    stCtlReg(12) := io.inM(12)
  }.elsewhen(io.addrM === 8194.asUInt() && io.writeM) {
    txBuff := io.inM(7, 0)
  }.otherwise {
    // Rx
    stCtlReg(5) := false.B
    // Tx
    stCtlReg(12) := false.B
  }

  /* Multiplexer */
  // if      addrM === 8192 then status and controll register of uart
  // else if addrM === 8193 then revieved data of UART Rx
  // else if addrM === 8194 then dummy data of UART Tx
  // else                        ram[addrM]
  io.out := MuxCase(
    ram.io.out,
    Seq(
      (io.addrM === 8192.asUInt()) -> stCtlReg.asUInt(),
      (io.addrM === 8193.asUInt()) -> rx.io.dout,
      (io.addrM === 8194.asUInt()) -> 0.asUInt()
    )
  )

  // Debug signal
  val debugReg = RegInit(0.asUInt())
  when(io.addrM === 1024.asUInt) {
    debugReg := ram.io.out
  }
  io.debug := debugReg
  io.rxdebug := rx.io.dout
}
