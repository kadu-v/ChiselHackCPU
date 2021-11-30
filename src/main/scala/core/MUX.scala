package core

import chisel3._

class MUX extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val sel = Input(Bool())

    val out = Output(UInt(16.W))
  })

  io.out := Mux(!io.sel, io.in1, io.in2)
}
