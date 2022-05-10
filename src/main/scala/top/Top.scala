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
  val mmio = Module(new MMIO(init, filename, words))

  // core
  core.io.inst := mmio.io.outInst
  core.io.inM := mmio.io.outRam
  core.io.run := mmio.io.run
  mmio.io.pc := core.io.pc // rom

  // Memory mapped IO
  // hack cpu core
  mmio.io.inRam := core.io.outM
  mmio.io.addrRam := core.io.addrM
  mmio.io.writeRam := core.io.writeM

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

  mmio.io.rx := io.rx
  io.rts := mmio.io.rts
  io.tx := mmio.io.tx
  mmio.io.cts := io.cts

  // SPI Master
  mmio.io.miso := io.miso
  io.mosi := mmio.io.mosi
  io.sclk := mmio.io.sclk
  io.csx := mmio.io.csx
  io.dcx := mmio.io.dcx // LCD monitor

  // Debug

  // LED
  val debug = mmio.io.debug === 8.asUInt

  io.debug := mmio.io.debug
  // io.rxdebug := mmio.io.rxdebug

  // LED
  io.GLED := reset.asBool // reset
  io.RLED1 := debug
  io.RLED2 := false.B
  io.RLED3 := false.B
  io.RLED4 := false.B
}
