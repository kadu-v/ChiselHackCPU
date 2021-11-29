package core

import chisel3._

class Core(n: Int = 4) extends Module {
  val io = new Bundle {
    val idata = Input(Bool())
  }
}
