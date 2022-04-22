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
    val debug2 = Output(UInt(16.W))
    val rxdebug = Output(UInt(16.W))
  })

  // Random Access Memory
  // negedge clock!!!
  // val ram = withClock((~clock.asBool()).asClock()) { Module(new RAM()) }
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

  // status register
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

  stCtlReg(0) := rx.io.busy // busy flag
  stCtlReg(1) := rx.io.recieved // recieved flag

  // write data to controll register
  when(io.addrM === 8192.asUInt() && io.writeM) {
    stCtlReg := VecInit(io.inM.asBools) // clear buffer flag
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
  io.debug := ram.io.debug
  io.debug2 := ram.io.debug2
  io.rxdebug := rx.io.dout
}
