package top

import core.Core
import memory.ROM

import chisel3._
import mmio.MMIO

class Top(filename: String, init: String, words: Int) extends Module {
  val io = IO(new Bundle {
    // UART RX Input
    val rx = Input(Bool())
    val rts = Output(Bool()) // request to send

    // UART Tx Output
    val tx = Output(Bool())
    val cts = Input(Bool()) // clear to send

    // SPI
    val miso = Input(Bool())
    val mosi = Output(Bool())
    val sclk = Output(Bool())
    val csx = Output(Bool())
    val dcx = Output(Bool()) // LCD monitor

    // LED
    val GLED = Output(Bool())
    val RLED1 = Output(Bool())
    val RLED2 = Output(Bool())
    val RLED3 = Output(Bool())
    val RLED4 = Output(Bool())

    // Debug signal
    val debug = Output(UInt(16.W))
    // val rxdebug = Output(UInt(16.W))
  })

  // Hack CPU core
  val core = Module(new Core())

  // MMIO
  val mem = Module(new MMIO(init))

  // ROM
  val rom = Module(new ROM(filename, words))

  // core
  core.io.inst := rom.io.out
  core.io.inM := mem.io.out

  // Memory mapped IO
  // hack cpu core
  mem.io.inM := core.io.outM
  mem.io.addrM := core.io.addrM
  mem.io.writeM := core.io.writeM

  // UART Rx and Tx
  //        io.rx                            io.tx
  //   +----------------+             +----------------+
  //   |                |             |                |
  //   |                v             v                |
  //  HOST           Hack Rx         HOST           Hack Tx
  //   ^                |             |                ^
  //   |                |             |                |
  //   +----------------+             +----------------+
  //        rx.io.rts                     rx.io.cts

  mem.io.rx := io.rx
  io.rts := mem.io.rts
  io.tx := mem.io.tx
  mem.io.cts := io.cts

  // SPI Master
  mem.io.miso := io.miso
  io.mosi := mem.io.mosi
  io.sclk := mem.io.sclk
  io.csx := mem.io.csx
  io.dcx := mem.io.dcx // LCD monitor

  // rom
  rom.io.addr := core.io.pc

  // Debug

  // LED
  val debug = mem.io.debug === 8.asUInt()

  io.debug := mem.io.debug
  // io.rxdebug := mem.io.rxdebug

  // LED
  io.GLED := reset.asBool() // reset
  io.RLED1 := debug
  io.RLED2 := false.B
  io.RLED3 := false.B
  io.RLED4 := false.B
}
