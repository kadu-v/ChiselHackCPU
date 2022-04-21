package hackcore

import chisel3._

class PCLoad extends Module {
  val io = IO(new Bundle {
    val zr = Input(Bool())
    val ng = Input(Bool())
    val j1 = Input(Bool())
    val j2 = Input(Bool())
    val j3 = Input(Bool())
    val pc_flag = Input(Bool())

    val load_pc = Output(Bool())
  })

  val not_zr = ~io.zr
  val not_ng = ~io.ng
  val not_j1 = ~io.j1
  val not_j2 = ~io.j2
  val not_j3 = ~io.j3

  val gt_zero = io.pc_flag & not_j1 & not_j2 & io.j3 & not_zr & not_ng
  val eq_zero = io.pc_flag & not_j1 & io.j2 & not_j3 & io.zr & not_ng
  val ge_zero = io.pc_flag & not_j1 & io.j2 & io.j3 & io.zr & not_ng
  val lt_zero = io.pc_flag & io.j1 & not_j2 & not_j3 & not_zr & io.ng
  val ne_zero = io.pc_flag & io.j1 & not_j2 & io.j3 & not_zr
  val le_zero = io.pc_flag & io.j1 & io.j2 & not_j3 & io.zr & io.ng
  val jp_zero = io.pc_flag & io.j1 & io.j2 & io.j3
  io.load_pc := gt_zero | eq_zero | ge_zero | lt_zero | ne_zero | le_zero | jp_zero
  // val x =
  // (not_j2 & io.j3 & not_zr & not_ng) | (not_j1 & io.j2 & io.zr & not_ng) | (io.j1 & io.j2 & io.j3) | (io.j1 & io.j2 & not_zr & io.ng) | (io.j1 & io.j2 & io.zr & io.ng)
  // io.load_pc := x & io.pc_flag
}
