package core

import chisel3._

class Decode extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(16.W))

    /* ジャンプのフラグはバスにしてまとめたほうがいいかも　*/
    val j1 = Output(Bool())
    val j2 = Output(Bool())
    val j3 = Output(Bool())

    val writeM = Output(Bool())
    val loadD = Output(Bool())
    val loadA = Output(Bool())

    /* ALU への入力はまとめてバスにしたほうがいいかも */
    val no = Output(Bool())
    val f = Output(Bool())
    val ny = Output(Bool())
    val zy = Output(Bool())
    val nx = Output(Bool())
    val zx = Output(Bool())

    val muxSel1 = Output(Bool())
    val muxSel2 = Output(Bool())
    val pcFlag = Output(Bool())
  })

  io.j1 := io.inst(2)
  io.j2 := io.inst(1)
  io.j3 := io.inst(0)
  io.writeM := io.inst(3) & io.inst(15)
  io.loadD := io.inst(4) & io.inst(15)
  io.loadA := (io.inst(5) & io.inst(15)) | ~io.inst(15)
  io.no := io.inst(6)
  io.f := io.inst(7)
  io.ny := io.inst(8)
  io.zy := io.inst(9)
  io.nx := io.inst(10)
  io.zx := io.inst(11)
  io.muxSel1 := io.inst(15)
  io.muxSel2 := io.inst(12) & io.inst(15)
  io.pcFlag := io.inst(15)
}
