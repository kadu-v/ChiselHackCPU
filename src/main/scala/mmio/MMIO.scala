package mmio

import chisel3._
import memory.RAM
import uart.Rx
import chisel3.util.MuxCase

class MMIO extends Module {
  val io = IO(new Bundle {
    val addr_m = Input(UInt(16.W))
    val write_m = Input(Bool())
    val in_m = Input(UInt(16.W))

    // Uart Input
    val rx = Input(Bool())

    // Output
    val out = Output(UInt(16.W))

    // Debug signal
    val debug = Output(UInt(16.W))
    val debug2 = Output(UInt(16.W))
  })

  // Random Access Memory
  // negedge clock!!!
  // val ram = withClock((~clock.asBool()).asClock()) { Module(new RAM()) }
  val ram = Module(new RAM())
  ram.io.addr := io.addr_m
  ram.io.in := io.in_m
  ram.io.write_m := Mux(
    io.write_m && io.addr_m(13) === 0.U,
    true.B,
    false.B
  )

  // Uart RX
  val rx = Module(new Rx(12, 115200))
  rx.io.rx := io.rx
  rx.io.cbf := Mux(
    io.write_m && io.addr_m(13) === 1.U && io.addr_m(3, 0) === "b0000".U,
    true.B,
    false.B
  )

  // Multiplexer
  io.out := MuxCase(ram.io.out, Seq((io.addr_m(13) === 1.U) -> rx.io.dout))

  // Debug signal
  io.debug := ram.io.debug
  io.debug2 := ram.io.debug2
}
