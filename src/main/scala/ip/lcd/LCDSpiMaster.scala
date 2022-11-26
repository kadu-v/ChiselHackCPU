package ip.lcd

import ip.interface.spi._
import chisel3._
import chisel3.util._

// Interface for MMIO
// ILI9341_DS_V1.13_20110805 33p
class LCDSpiMaster(stCtlAddr: Int, rxAddr: Int, txAddr: Int) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))
    val outM = Output(UInt(16.W)) // Output

    // SPI
    val miso = Input(Bool())
    val mosi = Output(Bool())
    val sclk = Output(Bool())
    val csx = Output(Bool()) // H: inactive, L: active
    val dcx = Output(Bool()) // H: Commadn, L: Data
    val rstx = Output(Bool()) // H: Running, L: Reset, external reset signal, perhaps this is not necessary.
  })
  /* SPI */
  //  15       12  Tx       8  7       4   Rx        0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //                             C  R  C         C B
  //                             M  U  L         O U
  //                             D  N  E         M S
  //                             /     A         P Y
  //                             D     R         L
  //                             A               E
  //                             T     B         T
  //                             A     U         E
  //                                   F
  //                                   F

  // staus and control register
  val spiStCtlReg = RegInit(
    VecInit(Seq.fill(16)(false.B))
  )

  // Master of SPI
  val master = Module(new Master())
  val txBuff = RegInit(0.asUInt)

  // MOSI Buffer
  when(io.addrM === txAddr.asUInt && io.writeM) {
    txBuff := io.inM(7, 0)
  }

  /* status register */
  spiStCtlReg(0) := master.io.busy
  spiStCtlReg(1) := master.io.completed

  /* control register */
  when(io.addrM === stCtlAddr.asUInt && io.writeM) {
    spiStCtlReg(4) := io.inM(4)
    spiStCtlReg(5) := io.inM(5)
    spiStCtlReg(6) := io.inM(6)
  }.otherwise {
    spiStCtlReg(4) := false.B
    spiStCtlReg(5) := false.B
    spiStCtlReg(6) := false.B
  }

  /* connect IO */
  master.io.cbf := spiStCtlReg(4)
  master.io.run := spiStCtlReg(5)
  master.io.inDcx := spiStCtlReg(6)
  master.io.txData := txBuff

  master.io.miso := io.miso
  io.mosi := master.io.mosi
  io.sclk := master.io.sclk
  io.csx := master.io.csx
  io.dcx := master.io.dcx
  io.rstx := true.B

  // Output
  io.outM := MuxCase(
    0.asUInt,
    Seq(
      (io.addrM === stCtlAddr.asUInt) -> spiStCtlReg.asUInt,
      (io.addrM === rxAddr.asUInt) -> master.io.rxData
    )
  )
}
