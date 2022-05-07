package ip.uart

import chisel3._
import chisel3.util._

class Rx(freq: Int, baudRate: Int) extends Module {
  val io = IO(new Bundle {
    // USRT RX Input
    val rx = Input(Bool()) // recieved serial data

    // control flag
    val cbf = Input(Bool()) // clear buffer

    // status
    val busy = Output(Bool()) // busy flag
    val recieved = Output(Bool()) // recieved flag

    // UART RX Output
    val rts = Output(Bool()) // request to send
    val dout = Output(UInt(16.W)) // recieved data
  })

  val waitTime =
    ((freq * 1000000) / baudRate).asUInt // 50 MHz / 115200 = 50 * 10**6 / 115200
  val halfTime =
    ((freq * 1000000) / baudRate / 2).asUInt // 50 MHz / 115200 / 2 = 50 * 10**6 / 115200

  // inner register for state machine
  val sIDLE :: sWAIT :: sRDATA :: sEND :: Nil = Enum(4)
  val rxData = RegInit(0.U(9.W))
  val state = RegInit(sIDLE)
  val clkCnt = RegInit(0.U(15.W))
  val dataCnt = RegInit(0.U(4.W))

  // inner register for status flag
  val busy = RegInit(false.B) // busy flag
  val recieved = RegInit(false.B) // recieved flag

  // inner register for UART RX
  val rts = RegInit(false.B) // H: inactive, L: active
  val buff = RegInit(0.U(16.W)) // inner buffer

  // detect positive edge of start bit rx
  val detedge0 = Module(new DetEdge())

  // connect register and output wire
  // status flag
  io.busy := busy
  io.recieved := recieved

  // UART RX Output
  io.rts := rts
  io.dout := buff

  // detedge
  detedge0.io.sigin := io.rx

  switch(state) {
    is(sIDLE) {
      when(io.cbf) {
        buff := "b0000000000000000".U // clear buffer
        recieved := false.B
      }.elsewhen(detedge0.io.negdet) {
        state := sWAIT
        buff := "b0000000000000000".U // clear buffer
        recieved := false.B
        busy := true.B
        rts := true.B
      }
    }
    is(sWAIT) {
      when(clkCnt === halfTime) {
        state := sRDATA
        clkCnt := 0.asUInt
      }.otherwise {
        clkCnt := clkCnt + 1.asUInt
      }
    }
    is(sRDATA) {
      when(dataCnt === 9.asUInt) {
        state := sEND
      }.elsewhen(clkCnt === waitTime) {
        clkCnt := 0.asUInt
        rxData := io.rx ## rxData(8, 1) // LSB
        dataCnt := dataCnt + 1.asUInt
      }.otherwise {
        clkCnt := clkCnt + 1.asUInt
      }
    }
    is(sEND) {
      state := sIDLE
      clkCnt := 0.asUInt
      dataCnt := 0.asUInt
      buff := "b00000000".U ## rxData(7, 0) // change data to valid data!!
      busy := false.B
      recieved := true.B
      rts := false.B
    }
  }
}
