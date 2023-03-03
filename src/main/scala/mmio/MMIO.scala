package mmio

import chisel3._
import ip.memory.{EBRAM, EBROM, ROM}
import ip.usb.USBUart
import ip.lcd.LCDSpiMaster
import ip.led.LED7Seg
import chisel3.util.MuxCase
import chisel3.experimental.Analog
import chisel3.experimental.attach
import chisel3.experimental._

class MMIO(
    freq: Int,
    init: String,
    file: String,
    romWords: Int,
    ramWords: Int,
    doTest: Boolean
) extends Module {
  val io = IO(new Bundle {
    /* Random Access Memory */
    // Input from core
    val addrRam = Input(UInt(16.W))
    val writeRam = Input(Bool())
    val inRam = Input(UInt(16.W))

    // Output to core
    val outRam = Output(UInt(16.W))

    /* USB Uart */
    // Rx
    val rx = Input(Bool())
    val rts = Output(Bool())
    // Tx
    val cts = Input(Bool())
    val tx = Output(Bool())

    /* LCD SPI Master */
    val miso = Input(Bool())
    val mosi = Output(Bool())
    val sclk = Output(Bool())
    val csx = Output(Bool())
    val dcx = Output(Bool()) // LCD monitor
    val rstx = Output(Bool())

    /* Read Only Memory */
    val pc = Input(UInt(16.W))
    val run = Output(Bool())
    val outInst = Output(UInt(16.W)) // Output to core

    /* SROM I/O */
    val SRAM_DATA = Analog(16.W)
    val SRAM_ADDR = Output(UInt(18.W))
    val SRAM_CSX = Output(Bool())
    val SRAM_OEX = Output(Bool())
    val SRAM_WEX = Output(Bool())

    /* LED 7 Segment */
    val outLED7seg = Output(UInt(7.W))
    val csLED7seg = Output(Bool())

    // LED
    val led0 = Output(Bool())
    val led1 = Output(Bool())

    // Debug signal
    val debug = Output(UInt(16.W))
  })

  /*----------------------------------------------------------------------------
   *                         Random Access Memory                              *
   ----------------------------------------------------------------------------*/
  val ram = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    Module(new EBRAM(init, ramWords))
  }
  // val ram = Module(new EBRAM(init, ramWords))

  ram.io.addrM := io.addrRam
  ram.io.inM := io.inRam
  ram.io.writeM := Mux(
    io.writeRam && io.addrRam(13) === 0.U,
    true.B,
    false.B
  )

  /*----------------------------------------------------------------------------
   *                         USB Uart                                          *
   ----------------------------------------------------------------------------*/
  val usbUart = Module(
    new USBUart(
      freq, // frequency of clock
      8192, // address of status and control register
      8193, // address of RX
      8194 // address of Tx
    )
  )
  usbUart.io.addrM := io.addrRam
  usbUart.io.inM := io.inRam
  usbUart.io.writeM := Mux(
    io.writeRam &&
      (io.addrRam === 8192.U
        || io.addrRam === 8193.U
        || io.addrRam === 8194.U),
    true.B,
    false.B
  )
  usbUart.io.rx := io.rx
  io.rts := usbUart.io.rts
  usbUart.io.cts := io.cts
  io.tx := usbUart.io.tx

  /*----------------------------------------------------------------------------
   *                         LCD SPI Master                                    *
   ----------------------------------------------------------------------------*/
  val lcdSpiMaster = Module(
    new LCDSpiMaster(
      8195, // address of status and control register
      8196, // address of miso
      8197 // address of mosi
    )
  )
  lcdSpiMaster.io.addrM := io.addrRam
  lcdSpiMaster.io.inM := io.inRam
  lcdSpiMaster.io.writeM := Mux(
    io.writeRam &&
      (io.addrRam === 8195.U
        || io.addrRam === 8196.U
        || io.addrRam === 8197.U),
    true.B,
    false.B
  )
  lcdSpiMaster.io.miso := io.miso
  io.mosi := lcdSpiMaster.io.mosi
  io.sclk := lcdSpiMaster.io.sclk
  io.csx := lcdSpiMaster.io.csx
  io.dcx := lcdSpiMaster.io.dcx
  io.rstx := lcdSpiMaster.io.rstx

  /*----------------------------------------------------------------------------
   *                         Read Only Memory for instructions                 *
   ----------------------------------------------------------------------------*/
  val rom = Module(
    new ROM(
      8198, // address of status and control register
      8199, // address of address register
      8200, // address of in register
      file,
      romWords,
      doTest
    )
  )
  rom.io.addrM := io.addrRam
  rom.io.writeM := io.writeRam
  rom.io.inM := io.inRam

  rom.io.pc := io.pc
  io.outInst := rom.io.outInst
  io.run := rom.io.run

  /* SROM I/O */

  if (doTest) {
    attach(rom.io.SRAM_DATA, io.SRAM_DATA)
  } else {
    attach(rom.io.SRAM_DATA, io.SRAM_DATA)
  }
  io.SRAM_ADDR := rom.io.SRAM_ADDR
  io.SRAM_CSX := rom.io.SRAM_CSX
  io.SRAM_OEX := rom.io.SRAM_OEX
  io.SRAM_WEX := rom.io.SRAM_WEX
  // attach(Wire(io.pin), Wire(rom.io.pin))
  // io.sromAddr := rom.io.addrM

  // val rom = Module(
  //   new EBROM(
  //     file,
  //     romWords
  //   )
  // )

  // rom.io.addrM := io.pc
  // io.outInst := rom.io.outM
  // io.run := false.B

  /*----------------------------------------------------------------------------
   *                         Debug LED                                         *
   ----------------------------------------------------------------------------*/

  val ledReg = RegInit(0.U(16.W))
  when(io.addrRam === 8201.asUInt && io.writeRam) {
    ledReg := io.inRam
  }
  io.led0 := ledReg(0)
  io.led1 := ledReg(1)

  /*----------------------------------------------------------------------------
   *                         LED 7 Segments                                    *
   ----------------------------------------------------------------------------*/

  val led7seg = Module(
    new LED7Seg(
      freq, // frequency of clock
      8202
    )
  )

  led7seg.io.addrM := io.addrRam
  led7seg.io.writeM := io.writeRam
  led7seg.io.inM := io.inRam

  io.outLED7seg := led7seg.io.out
  io.csLED7seg := led7seg.io.cs

  /*----------------------------------------------------------------------------
   *                         Multiplexer                                       *
   ----------------------------------------------------------------------------*/

  io.outRam := MuxCase(
    ram.io.outM.asUInt,
    Seq(
      (io.addrRam === 8192.asUInt
        || io.addrRam === 8193.asUInt
        || io.addrRam === 8194.asUInt) -> usbUart.io.outM,
      (io.addrRam === 8195.asUInt
        || io.addrRam === 8196.asUInt
        || io.addrRam === 8197.asUInt) -> lcdSpiMaster.io.outM,
      (io.addrRam === 8198.asUInt
        || io.addrRam === 8199.asUInt
        || io.addrRam === 8200.asUInt) -> rom.io.outM,
      (io.addrRam > 8200.asUInt) -> 0.asUInt
    )
  )

  /*----------------------------------------------------------------------------
   *                         Debug signal                                      *
   ----------------------------------------------------------------------------*/
  val debugReg = RegInit(0.asUInt)
  when(io.addrRam === 1024.asUInt) {
    debugReg := ram.io.outM
  }
  io.debug := debugReg
}
