package core

import chisel3._

class ALU extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val zx = Input(Bool())
    val nx = Input(Bool())
    val zy = Input(Bool())
    val ny = Input(Bool())
    val f = Input(Bool())
    val no = Input(Bool())

    val out = Output(UInt(16.W))
    val zr = Output(Bool())
    val ng = Output(Bool())
  })

  val x0 = Mux(io.zx, 0.asUInt(), io.in1)
  val x1 = Mux(io.nx, ~x0, x0)

  val y0 = Mux(io.zy, 0.asUInt(), io.in2)
  val y1 = Mux(io.ny, ~y0, y0)

  val z0 = Mux(io.f, x1 + y1, x1 | y1)
  io.out := Mux(io.no, ~z0, z0)

  io.zr := Mux(z0 === 0.asUInt(), true.B, false.B)
  io.ng := Mux(z0 < 0.asUInt(), true.B, false.B)
}
