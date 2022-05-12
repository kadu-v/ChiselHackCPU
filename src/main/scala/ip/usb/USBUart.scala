package ip.usb

import ip.interface.uart._
import chisel3._
import chisel3.util._

// Interface for MMIO
class USBUart(stCtlAddr: Int, rxAddr: Int, txAddr: Int) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))

    // UART
    // Rx
    val rx = Input(Bool())
    val rts = Output(Bool())

    // Tx
    val cts = Input(Bool())
    val tx = Output(Bool())

    // Output
    val out = Output(UInt(16.W))
  })

  /* UART */
  //  15       12  Tx       8  7       4   Rx        0
  // |-----------------------------------------------|
  // |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
  // |-----------------------------------------------|
  //            R          B            C        R  B
  //            U          U            L        E  U
  //            N          S            E        C  S
  //                       Y            A        I  Y
  //                                    R        E
  //                       T                     V  R
  //                       X                     E  X

  // staus and control register
  val uartStCtlReg = RegInit(
    VecInit(Seq.fill(16)(false.B))
  )

  //  RX
  val rx = Module(new Rx(12, 115200))

  // Tx
  val tx = Module(new Tx(12, 115200))
  val txBuff = RegInit(0.asUInt) // buffer for Tx

  // Tx Buffer
  when(io.addrM === txAddr.asUInt && io.writeM) {
    txBuff := io.inM(7, 0)
  }

  /* status register */
  // Rx
  uartStCtlReg(0) := rx.io.busy // busy flag
  uartStCtlReg(1) := rx.io.recieved // recieved flag
  // Tx
  uartStCtlReg(8) := tx.io.busy

  /* control register */
  when(io.addrM === stCtlAddr.asUInt && io.writeM) {
    uartStCtlReg(4) := io.inM(4) // Rx
    uartStCtlReg(12) := io.inM(12) // Tx
  }.otherwise {
    uartStCtlReg(4) := false.B // Rx
    uartStCtlReg(12) := false.B // Tx
  }

  /* connect IO */
  rx.io.rx := io.rx
  io.rts := rx.io.rts
  rx.io.cbf := uartStCtlReg(4)

  tx.io.din := txBuff
  tx.io.run := uartStCtlReg(12)
  tx.io.cts := io.cts
  io.tx := tx.io.tx

  // Output
  io.out := MuxCase(
    0.asUInt,
    Seq(
      (io.addrM === stCtlAddr.asUInt) -> uartStCtlReg.asUInt,
      (io.addrM === rxAddr.asUInt) -> rx.io.dout
    )
  )
}
