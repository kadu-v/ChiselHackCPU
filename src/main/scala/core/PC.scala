package core

import chisel3._

class PC extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val load = Input(Bool())
    val inc = Input(Bool())
    val run = Input(Bool())

    val out = Output(UInt(16.W))
  })

  val inner = withClock((~clock.asBool()).asClock()) { RegInit(0.asUInt) }
  io.out := inner
  when(io.run) {
    inner := 0.asUInt
  }.elsewhen(io.load) {
    inner := io.a
  }.elsewhen(io.inc) {
    inner := inner + 1.asUInt
  }

}
