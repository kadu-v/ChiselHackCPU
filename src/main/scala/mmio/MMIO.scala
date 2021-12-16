package mmio

import chisel3._
import memory.RAM
import uart.Rx
import chisel3.util.MuxCase

class MMIO extends Module {
  val io = IO(new Bundle {
    val addr_m = Input(UInt(16.W))
    val load_m = Input(Bool())
    val out_m = Input(UInt(16.W))

    val out = Output(UInt(16.W))
  })

  val buff = RegInit(0.U)
  io.out := buff

  // Random Access Memory
  // negedge clock!!!
  // val ram = withClock((~clock.asBool()).asClock()) { Module(new RAM()) }
  val ram = Module(new RAM())
  ram.io.addr := io.addr_m
  rx.ram.load_m := Mux(
    io.load_m && io.addr_m(13) === 0.U,
    true.B,
    false.B
  )

  // Uart RX
  val rx = Module(new Rx())
  rx.io.addr := io.addr_m
  rx.io.load_m := Mux(
    io.load_m && io.addr_m(13) === 1.U,
    true.B,
    false.B
  )

  // Multiplexer
  io.out := MuxCase(ram.io.out, (io.addr_m(13) === 1.U) -> rx.io.dout)
}
