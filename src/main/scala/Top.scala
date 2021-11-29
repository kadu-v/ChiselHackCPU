package blink

import chisel3._

class Top extends Module {
  val io = IO(new Bundle {
    val PIO0 = Output(Bool())
  })
  io.PIO0 := false.B
}

object Elaborate extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Top(), args)
}
