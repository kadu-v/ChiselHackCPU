package top

import core.Core

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

    // LED 7 Segment
    val outLED7Seg = Output(UInt(7.W))
    val csLED7Seg = Output(Bool())

    // Debug LED
    // val GLED = Output(Bool())
    // val RLED1 = Output(Bool())
    // val RLED2 = Output(Bool())
    // val RLED3 = Output(Bool())
    // val RLED4 = Output(Bool())

    // Debug signal
    val debug = Output(UInt(16.W))
    // val rxdebug = Output(UInt(16.W))
  })

  val div2Clk = RegInit(0.asUInt(1.W))
  div2Clk := div2Clk + 1.asUInt

  // Hack CPU core
  val core = withClock(div2Clk.asBool.asClock) {
    Module(new Core())
  }

  // MMIO
  val mmio = withClock(div2Clk.asBool.asClock) {
    Module(new MMIO(6, init, filename, words))
  }

  // Hack CPU core and MMIO
  // val core = Module(new Core())
  // val mmio = Module(new MMIO(6, init, filename, words))

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

  /*----------------------------------------------------------------------------
   *                         USB Uart                                          *
   ----------------------------------------------------------------------------*/
  mmio.io.rx := io.rx
  io.rts := mmio.io.rts
  io.tx := mmio.io.tx
  mmio.io.cts := io.cts

  /*----------------------------------------------------------------------------
   *                         LCD SPI Master                                    *
   ----------------------------------------------------------------------------*/
  mmio.io.miso := io.miso
  io.mosi := mmio.io.mosi
  io.sclk := mmio.io.sclk
  io.csx := mmio.io.csx
  io.dcx := mmio.io.dcx // LCD monitor

  /*----------------------------------------------------------------------------
   *                         LED 7 Segments                                    *
   ----------------------------------------------------------------------------*/
  io.outLED7Seg := mmio.io.outLED7seg
  io.csLED7Seg := mmio.io.csLED7seg

  // LED
  val debug = mmio.io.debug === 8.asUInt

  io.debug := mmio.io.debug
  // io.rxdebug := mmio.io.rxdebug

  // LED
  // io.GLED := reset.asBool // reset
  // io.RLED1 := debug
  // io.RLED2 := false.B
  // io.RLED3 := false.B
  // io.RLED4 := false.B
}
