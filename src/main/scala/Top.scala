package top

import hackcore.Core
import memory.ROM

import chisel3._
import mmio.MMIO

class Top(filename: String, init: String) extends Module {
  val io = IO(new Bundle {
    // UART RX Input
    val rx = Input(Bool())
    val rts = Output(Bool())

    val GPIO = Output(Bool())

    // Debug signal
    val debug = Output(UInt(16.W))
    val debug2 = Output(UInt(16.W))
    val rxdebug = Output(UInt(16.W))
  })

  // Hack CPU core
  val core = Module(new Core())

  // MMIO
  val mem = Module(new MMIO(init))

  // ROM
  val rom = Module(new ROM(filename))

  // core
  core.io.inst := rom.io.out
  core.io.inM := mem.io.out

  // Memory mapped IO
  // hack cpu core
  mem.io.inM := core.io.outM
  mem.io.addrM := core.io.addrM
  mem.io.writeM := core.io.writeM

  // UART Rx
  //        io.rx
  //   +----------------+
  //   |                |
  //   |                v
  //  HOST           Hack Rx
  //   ^                |
  //   |                |
  //   +----------------+
  //        rx.io.rts
  mem.io.rx := io.rx
  io.rts := mem.io.rts

  // Debug signal
  io.debug := mem.io.debug
  io.debug2 := mem.io.debug2
  io.rxdebug := mem.io.rxdebug

  // rom
  rom.io.addr := core.io.pc

  io.GPIO := core.io.writeM

}

object Elaborate extends App {
  val argsx = args :+ "--target-dir" :+ "out"
  (new chisel3.stage.ChiselStage)
    .emitVerilog(new Top("./bin.hack", "./init.bin"), argsx)
}
