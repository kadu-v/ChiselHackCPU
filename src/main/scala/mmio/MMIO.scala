package mmio

import chisel3._
import ip.memory.{EBRAM, ROM}
import ip.usb.USBUart
import ip.lcd.LCDSpiMaster
import ip.led.LED7Seg
import chisel3.util.MuxCase

class MMIO(freq: Int, init: String, file: String, words: Int) extends Module {
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

    /* Program Counter */
    val pc = Input(UInt(16.W))
    val run = Output(Bool())

    // Output to core
    val outInst = Output(UInt(16.W))

    // Debug signal
    val debug = Output(UInt(16.W))
  })

  /*----------------------------------------------------------------------------
   *                         Random Access Memory                              *
   ----------------------------------------------------------------------------*/
  val ram = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    Module(new EBRAM(init))
  }
  ram.io.addr := io.addrRam
  ram.io.in := io.inRam
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
      (io.addrRam === 8194.U
        || io.addrRam === 8195.U
        || io.addrRam === 8196.U),
    true.B,
    false.B
  )
  lcdSpiMaster.io.miso := io.miso
  io.mosi := lcdSpiMaster.io.mosi
  io.sclk := lcdSpiMaster.io.sclk
  io.csx := lcdSpiMaster.io.csx
  io.dcx := lcdSpiMaster.io.dcx

  /*----------------------------------------------------------------------------
   *                         Read Only Memory for instructions                 *
   ----------------------------------------------------------------------------*/
  val rom = Module(
    new ROM(
      8197, // address of status and control register
      8198, // address of address register
      8199, // address of in register
      file,
      words
    )
  )
  rom.io.addrM := io.addrRam
  rom.io.writeM := io.writeRam
  rom.io.inM := io.inRam

  rom.io.pc := io.pc
  io.outInst := rom.io.outInst
  io.run := rom.io.run

  /*----------------------------------------------------------------------------
   *                         LED 7 Segments                                    *
   ----------------------------------------------------------------------------*/

  /*----------------------------------------------------------------------------
   *                         Multiplexer                                       *
   ----------------------------------------------------------------------------*/
  io.outRam := MuxCase(
    ram.io.out,
    Seq(
      (io.addrRam === 8192.asUInt
        || io.addrRam === 8193.asUInt
        || io.addrRam === 8194.asUInt) -> usbUart.io.out,
      (io.addrRam === 8195.asUInt
        || io.addrRam === 8196.asUInt
        || io.addrRam === 8197.asUInt) -> lcdSpiMaster.io.out,
      (io.addrRam === 8197.asUInt
        || io.addrRam === 8198.asUInt
        || io.addrRam === 8199.asUInt) -> rom.io.out
    )
  )

  /*----------------------------------------------------------------------------
   *                         Debug signal                                      *
   ----------------------------------------------------------------------------*/
  val debugReg = RegInit(0.asUInt)
  when(io.addrRam === 1024.asUInt) {
    debugReg := ram.io.out
  }
  io.debug := debugReg
}
