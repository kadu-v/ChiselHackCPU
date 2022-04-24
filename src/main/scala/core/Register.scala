package core

import chisel3.Bundle
import Chisel.UInt
import chisel3._

class Register extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val load = Input(Bool())

    val out = Output(UInt(16.W))
  })

  val reg = withClock((~clock.asBool()).asClock()) { RegInit(0.asUInt()) }
  when(io.load) {
    reg := io.in
  }

  io.out := reg
}
