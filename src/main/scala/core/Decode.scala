package core

import chisel3._

class Decode extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(16.W))

    val j1 = Output(Bool())
    val j2 = Output(Bool())
    val j3 = Output(Bool())
    val write_m = Output(Bool())
    val load_d = Output(Bool())
    val load_a = Output(Bool())
    val no = Output(Bool())
    val f = Output(Bool())
    val ny = Output(Bool())
    val zy = Output(Bool())
    val nx = Output(Bool())
    val zx = Output(Bool())
    val mux_sel1 = Output(Bool())
    val mux_sel2 = Output(Bool())
    val pc_flag = Output(Bool())
  })

  io.j1 := io.inst(2)
  io.j2 := io.inst(1)
  io.j3 := io.inst(0)
  io.write_m := io.inst(3) & io.inst(15)
  io.load_d := io.inst(4) & io.inst(15)
  io.load_a := io.inst(5) & ~io.inst(15)
  io.no := io.inst(6)
  io.f := io.inst(7)
  io.ny := io.inst(8)
  io.zy := io.inst(9)
  io.nx := io.inst(10)
  io.zx := io.inst(11)
  io.mux_sel1 := io.inst(12) & io.inst(15)
  io.mux_sel2 := io.inst(15)
  io.pc_flag := io.inst(15)
}
