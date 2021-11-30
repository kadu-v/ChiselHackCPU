package core

import Chisel._

class PC extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val load = Input(Bool())
    val inc = Input(Bool())

    val out = Output(UInt(16.W))
  })

  val inner = RegInit(0.asUInt())
  io.out := inner
  when(io.load) {
    inner := io.a
  }.elsewhen(io.inc) {
    inner := inner + 1.asUInt()
  }

}
