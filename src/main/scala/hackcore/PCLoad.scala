package hackcore

import chisel3._

class PCLoad extends Module {
  val io = IO(new Bundle {
    val zr = Input(Bool())
    val ng = Input(Bool())
    val j1 = Input(Bool())
    val j2 = Input(Bool())
    val j3 = Input(Bool())
    val pcFlag = Input(Bool())

    val loadPC = Output(Bool())
  })

  val noZr = ~io.zr
  val notNg = ~io.ng
  val notJ1 = ~io.j1
  val notJ2 = ~io.j2
  val notJ3 = ~io.j3

  val gtZero = io.pcFlag & notJ1 & notJ2 & io.j3 & noZr & notNg
  val eqZero = io.pcFlag & notJ1 & io.j2 & notJ3 & io.zr & notNg
  val geZero = io.pcFlag & notJ1 & io.j2 & io.j3 & io.zr & notNg
  val ltZero = io.pcFlag & io.j1 & notJ2 & notJ3 & noZr & io.ng
  val neZero = io.pcFlag & io.j1 & notJ2 & io.j3 & noZr
  val leZero = io.pcFlag & io.j1 & io.j2 & notJ3 & io.zr & io.ng
  val jpZero = io.pcFlag & io.j1 & io.j2 & io.j3
  io.loadPC := gtZero | eqZero | geZero | ltZero | neZero | leZero | jpZero
  // val x =
  // (notJ2 & io.j3 & noZr & notNg) | (notJ1 & io.j2 & io.zr & notNg) | (io.j1 & io.j2 & io.j3) | (io.j1 & io.j2 & noZr & io.ng) | (io.j1 & io.j2 & io.zr & io.ng)
  // io.loadPC := x & io.pcFlag
}
