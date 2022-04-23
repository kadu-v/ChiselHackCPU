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

    //
    // val PIO0 = Output(UInt(8.W))
    //
    // val PIO1 = Output(UInt(8.W))

    // LED
    val GLED = Output(Bool())
    val RLED1 = Output(Bool())
    val RLED2 = Output(Bool())
    val RLED3 = Output(Bool())
    val RLED4 = Output(Bool())

    // Debug signal
    // val debug = Output(UInt(8.W))
    // val rxdebug = Output(UInt(16.W))
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

  // rom
  rom.io.addr := core.io.pc

  // Debug

  // LED
  val debug = mem.io.debug === 8.asUInt()

  // io.debug := mem.io.debug
  // io.rxdebug := mem.io.rxdebug

  // LED
  io.GLED := reset.asBool() // reset
  io.RLED1 := debug
  io.RLED2 := false.B
  io.RLED3 := false.B
  io.RLED4 := false.B
}

object Elaborate extends App {
  val argsx = args :+ "--target-dir" :+ "apio" :+ "--emission-options=disableMemRandomization,disableRegisterRandomization"
  (new chisel3.stage.ChiselStage)
    .emitVerilog(new Top("bin.hack", "init.bin"), argsx)
}
