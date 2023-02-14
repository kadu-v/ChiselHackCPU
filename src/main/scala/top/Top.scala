package top

import core.Core

import chisel3._
import mmio.MMIO
import chisel3.experimental.Analog
import chisel3.experimental.attach

class Top(
    filename: String,
    init: String,
    romWords: Int,
    ramWords: Int,
    doTest: Boolean
) extends Module {
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
    val rstx = Output(Bool())
    val ledback = Output(Bool())

    // LED 7 Segment
    val outLED7Seg = Output(UInt(7.W))
    val csLED7Seg = Output(Bool())

    // Debug LED
    val led0 = Output(Bool())
    val led1 = Output(Bool())

    // SROM
    // val sromAddr = Output(UInt(16.W))
    // val pin = Analog(16.W)

    // Buttun
    // val btn0 = Input(Bool())
    // val btn1 = Input(Bool())

    // Debug signal
    // val debug = Output(UInt(16.W))
    // val rxdebug = Output(UInt(16.W))
  })

  val div4Clk = RegInit(0.asUInt(2.W))
  when(div4Clk === "b11".asUInt) {
    div4Clk := 0.asUInt
  }.otherwise {
    div4Clk := div4Clk + 1.asUInt
  }
  val div8Clk = RegInit(0.U(3.W))
  when(div8Clk === "b111".asUInt) {
    div8Clk := 0.asUInt
  }.otherwise {
    div8Clk := div8Clk + 1.asUInt
  }

  val rst = RegInit(true.B)
  val cnt = RegInit(0.U(3.W))
  when(cnt === "b111".U) {
    rst := false.B
  }.otherwise {
    cnt := cnt + 1.asUInt
  }

  // Hack CPU core
  val core = withClockAndReset(div4Clk(1).asBool.asClock, rst) {
    Module(new Core())
  }

  // MMIO
  val mmio = withClockAndReset(div4Clk(1).asBool.asClock, rst) {
    Module(new MMIO(25, init, filename, romWords, ramWords, doTest))
  }

  // val core = Module(new Core())
  // val mmio = Module(new MMIO(100, init, filename, words))

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
  io.rstx := mmio.io.rstx
  io.ledback := true.B

  /*----------------------------------------------------------------------------
   *                         LED 7 Segments                                    *
   ----------------------------------------------------------------------------*/
  io.outLED7Seg := mmio.io.outLED7seg
  io.csLED7Seg := mmio.io.csLED7seg

  /*----------------------------------------------------------------------------
   *                         Debug LED                                         *
   ----------------------------------------------------------------------------*/
  io.led0 := mmio.io.led0
  io.led1 := mmio.io.led1

  /*----------------------------------------------------------------------------
   *                         SROM                                              *
   ----------------------------------------------------------------------------*/
  // attach(Wire(io.pin), Wire(mmio.io.pin))
  // io.sromAddr := mmio.io.sromAddr

  // val debug = mmio.io.debug === 8.asUInt

  // io.led0 := mmio.io.debug
  // io.led1 := false.B

  // io.debug := mmio.io.debug

}
