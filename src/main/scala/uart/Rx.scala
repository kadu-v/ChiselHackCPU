package uart

import chisel3._
import chisel3.util._

class Rx(freq: Int, baudRate: Int) extends Module {
  val io = IO(new Bundle {
    val rx = Input(Bool()) // received serial data
    val cbf = Input(Bool()) // clear buffer

    val rts = Output(Bool()) // request to send
    val dout = Output(UInt(16.W)) // received data
  })

  val waitTime =
    ((freq * 1000000) / baudRate)
      .asUInt() // 50 MHz / 115200 = 50 * 10**6 / 115200
  val halfTime =
    ((freq * 1000000) / baudRate / 2)
      .asUInt() // 50 MHz / 115200 / 2 = 50 * 10**6 / 115200

  val sIDLE :: sWAIT :: sRDATA :: sEND :: Nil = Enum(4)
  val rxData = RegInit(0.U(9.W))
  val state = RegInit(sIDLE)
  val clkCnt = RegInit(0.U(15.W))
  val dataCnt = RegInit(0.U(4.W))
  val rts = RegInit(false.B) // H: inactive, L: active
  val buff = RegInit(0.U(16.W))

  // detect positive edge of start bit rx
  val detedge0 = Module(new DetEdge())

  detedge0.io.sigin := io.rx
  io.rts := rts
  io.dout := buff

  switch(state) {
    is(sIDLE) {
      when(io.cbf) {
        buff := Cat(1.U, buff(14, 0)) // change data to invalid data!!
      }.elsewhen(detedge0.io.negdet) {
        buff := Cat(1.U, buff(14, 0))
        state := sWAIT
        rts := true.B
      }
    }
    is(sWAIT) {
      when(clkCnt === halfTime) {
        state := sRDATA
        clkCnt := 0.asUInt()
      }.otherwise {
        clkCnt := clkCnt + 1.asUInt()
      }
    }
    is(sRDATA) {
      when(dataCnt === 9.asUInt()) {
        state := sEND
      }.elsewhen(clkCnt === waitTime) {
        clkCnt := 0.asUInt()
        rxData := Cat(io.rx, rxData(8, 1)) // LSB
        dataCnt := dataCnt + 1.asUInt()
      }.otherwise {
        clkCnt := clkCnt + 1.asUInt()
      }
    }
    is(sEND) {
      state := sIDLE
      clkCnt := 0.asUInt()
      dataCnt := 0.asUInt()
      buff := Cat("b00000000".U, rxData(7, 0)) // change data to valid data!!
      rts := false.B
    }
  }
}
