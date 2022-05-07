package mmio

import chisel3._
import memory.RAM
import usb.USBUart
import lcd.LCDSpiMaster
import chisel3.util.MuxCase

class MMIO(init: String) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    /* usbUart */
    // Rx
    val rx = Input(Bool())
    val rts = Output(Bool())
    // Tx
    val cts = Input(Bool())
    val tx = Output(Bool())

    /* lcdSpiMaster */
    val miso = Input(Bool())
    val mosi = Output(Bool())
    val sclk = Output(Bool())
    val csx = Output(Bool())
    val dcx = Output(Bool()) // LCD monitor

    // Output to core
    val out = Output(UInt(16.W))

    // Debug signal
    val debug = Output(UInt(16.W))
  })

  /* Random Access Memory */
  val ram = withClock((~clock.asBool()).asClock()) { // negedge clock!!!
    Module(new RAM(init))
  }
  ram.io.addr := io.addrM
  ram.io.in := io.inM
  ram.io.writeM := Mux(
    io.writeM && io.addrM(13) === 0.U,
    true.B,
    false.B
  )

  /* usbUart */
  val usbUart = Module(
    new USBUart(
      8192, // address of status and control register
      8193, // address of RX
      8194 // address of Tx
    )
  )
  usbUart.io.addrM := io.addrM
  usbUart.io.inM := io.inM
  usbUart.io.writeM := Mux(
    io.writeM &&
      (io.addrM === 8192.U
        || io.addrM === 8193.U
        || io.addrM === 8194.U),
    true.B,
    false.B
  )
  usbUart.io.rx := io.rx
  io.rts := usbUart.io.rts
  usbUart.io.cts := io.cts
  io.tx := usbUart.io.tx

  /* lcdSpiMaster */
  val lcdSpiMaster = Module(
    new LCDSpiMaster(
      8195, // address of status and control register
      8196, // address of miso
      8197 // address of mosi
    )
  )
  lcdSpiMaster.io.addrM := io.addrM
  lcdSpiMaster.io.inM := io.inM
  lcdSpiMaster.io.writeM := Mux(
    io.writeM &&
      (io.addrM === 8194.U
        || io.addrM === 8195.U
        || io.addrM === 8196.U),
    true.B,
    false.B
  )
  lcdSpiMaster.io.miso := io.miso
  io.mosi := lcdSpiMaster.io.mosi
  io.sclk := lcdSpiMaster.io.sclk
  io.csx := lcdSpiMaster.io.csx
  io.dcx := lcdSpiMaster.io.dcx

  /* Multiplexer */
  // if      addrM === 8192 then status and control register of usbUart
  // else if addrM === 8193 then revieved data of usbUart Rx
  // else if addrM === 8194 then dummy data of usbUart Tx
  // else                        ram[addrM]
  io.out := MuxCase(
    ram.io.out,
    Seq(
      (io.addrM === 8192.asUInt
        || io.addrM === 8193.asUInt
        || io.addrM === 8194.asUInt) -> usbUart.io.out,
      (io.addrM === 8195.asUInt
        || io.addrM === 8196.asUInt
        || io.addrM === 8197.asUInt) -> lcdSpiMaster.io.out
    )
  )

  // Debug signal
  val debugReg = RegInit(0.asUInt)
  when(io.addrM === 1024.asUInt) {
    debugReg := ram.io.out
  }
  io.debug := debugReg
}
