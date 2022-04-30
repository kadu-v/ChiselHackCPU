package mmio

import chisel3._
import memory.RAM
import uart.Uart
import spi.Spi
import chisel3.util.MuxCase

class MMIO(init: String) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    /* UART */
    // Rx
    val rx = Input(Bool())
    val rts = Output(Bool())
    // Tx
    val cts = Input(Bool())
    val tx = Output(Bool())

    /* SPI */
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

  /* UART */
  val uart = Module(
    new Uart(
      8192, // address of status and control register
      8193, // address of RX
      8194 // address of Tx
    )
  )
  uart.io.addrM := io.addrM
  uart.io.inM := io.inM
  uart.io.writeM := Mux(
    io.writeM &&
      (io.addrM === 8192.U
        || io.addrM === 8193.U
        || io.addrM === 8194.U),
    true.B,
    false.B
  )
  uart.io.rx := io.rx
  io.rts := uart.io.rts
  uart.io.cts := io.cts
  io.tx := uart.io.tx

  /* SPI */
  val spi = Module(
    new Spi(
      8195, // address of status and control register
      8196, // address of miso
      8197 // address of mosi
    )
  )
  spi.io.addrM := io.addrM
  spi.io.inM := io.inM
  spi.io.writeM := Mux(
    io.writeM &&
      (io.addrM === 8194.U
        || io.addrM === 8195.U
        || io.addrM === 8196.U),
    true.B,
    false.B
  )
  spi.io.miso := io.miso
  io.mosi := spi.io.mosi
  io.sclk := spi.io.sclk
  io.csx := spi.io.csx
  io.dcx := spi.io.dcx

  /* Multiplexer */
  // if      addrM === 8192 then status and control register of uart
  // else if addrM === 8193 then revieved data of UART Rx
  // else if addrM === 8194 then dummy data of UART Tx
  // else                        ram[addrM]
  io.out := MuxCase(
    ram.io.out,
    Seq(
      (io.addrM === 8192.asUInt()
        || io.addrM === 8193.asUInt()
        || io.addrM === 8194.asUInt()) -> uart.io.out,
      (io.addrM === 8195.asUInt()
        || io.addrM === 8196.asUInt()
        || io.addrM === 8197.asUInt()) -> spi.io.out
    )
  )

  // Debug signal
  val debugReg = RegInit(0.asUInt())
  when(io.addrM === 1024.asUInt) {
    debugReg := ram.io.out
  }
  io.debug := debugReg
}
