package mmio

import chisel3._
import memory.RAM
import uart.Rx
import chisel3.util.MuxCase

class MMIO(init: String) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    // Uart Input
    val rx = Input(Bool())
    val rts = Output(Bool())

    // Output to core
    val out = Output(UInt(16.W))

    // Debug signal
    val debug = Output(UInt(16.W))
    val rxdebug = Output(UInt(16.W))
  })

  // Random Access Memory
  // negedge clock!!!
  val ram = withClock((~clock.asBool()).asClock()) { Module(new RAM(init)) }
  ram.io.addr := io.addrM
  ram.io.in := io.inM
  ram.io.writeM := Mux(
    io.writeM && io.addrM(13) === 0.U,
    true.B,
    false.B
  )

  // UART
  // staus and controll register
  val stCtlReg = RegInit(
    VecInit(Seq.fill(16)(false.B))
  )

  // Uart RX
  val rx = Module(new Rx(12, 115200))
  rx.io.rx := io.rx
  io.rts := rx.io.rts
  rx.io.cbf := stCtlReg(5)

  //  15       Tx          8  7         4 Rx        0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //                                 C  R        R B
  //                                 L  E        E U
  //                                 E  S        C S
  //                                 A  E        I Y
  //                                 R  T        E
  //                                             V
  //                                             E

  // status register
  stCtlReg(0) := rx.io.busy // busy flag
  stCtlReg(1) := rx.io.recieved // recieved flag

  // controll register
  when(io.addrM === 8192.asUInt() && io.writeM) {
    stCtlReg(4) := io.inM(4)
    stCtlReg(5) := io.inM(5)
  } otherwise {
    stCtlReg(5) := false.B
  }

  // Multiplexer
  io.out := MuxCase(
    ram.io.out,
    Seq(
      (io.addrM === 8192.asUInt()) -> stCtlReg.asUInt(),
      (io.addrM === 8193.asUInt()) -> rx.io.dout
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
