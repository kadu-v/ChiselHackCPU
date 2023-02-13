package ip.interface.spi

import chisel3._
import chisel3.util._

// mode 0
// CPOL = 0, CPHA = 0
// MSB
class Master extends Module {
  val io = IO(new Bundle {
    val cbf = Input(Bool()) // clear buffer flag
    val lenOfData = Input(Bool()) // H: 16bit, L: 8bit
    val run = Input(Bool()) // H: start runnning, L: idle
    val inDcx = Input(Bool()) // H: Data, L: Command
    val txData = Input(UInt(16.W)) // trasmitted data
    val rxData = Output(UInt(16.W)) // recived data
    val busy = Output(Bool()) // busy flag
    val completed = Output(Bool())

    // SPI Interface
    val miso = Input(Bool()) // Master In Slave Out
    val mosi = Output(Bool()) // Master Out Slave In
    val sclk = Output(Bool()) // clock
    val csx = Output(Bool()) // H: inactive, L: active

    // LCD Monitor
    val dcx = Output(Bool()) // H: Data, L: Command
  })

  val sIDLE :: sRUN :: sEND :: Nil = Enum(3) // enumerate for state machine
  val state = RegInit(sIDLE) // inner register for MISO
  val stateSclk = RegInit(sIDLE) // inner register for SCLK
  val stateCsx = RegInit(sIDLE) // inner register for CSX

  val rxReg = RegInit(0.asUInt)
  val txReg = RegInit(0.U(16.W))
  val lenOfData = RegInit(false.B) // H: 16bit, L: 8bit
  val sclkReg = RegInit(false.B)
  val csxReg = RegInit(true.B) // H: inactionve, L: active
  val busy = RegInit(false.B) // H: busy, L: idle
  val rxBuff = RegInit(0.asUInt) // inner buffer
  val completed = RegInit(
    true.B
  ) // H: complete reciveing data, L: incomplete reciveing data
  val dcx = RegInit(false.B) // H: Data, L: Command

  // counter
  val count = RegInit(0.U(6.W))
  val countSclk = RegInit(0.U(6.W))
  val countCsx = RegInit(0.U(6.W))

  // connect io
  io.rxData := rxReg
  io.busy := busy
  io.completed := completed

  io.mosi := Mux(lenOfData, txReg(15), txReg(7))
  io.sclk := sclkReg
  io.csx := csxReg
  io.dcx := dcx

  // SCLK
  switch(stateSclk) {
    is(sIDLE) {
      when(io.run) {
        stateSclk := sRUN
      }
    }
    is(sRUN) {
      sclkReg := ~sclkReg
      when(countSclk === 15.asUInt && ~lenOfData) {
        stateSclk := sEND
      }.elsewhen(countSclk === 31.asUInt && lenOfData) {
        stateSclk := sEND
      }.otherwise {
        countSclk := countSclk + 1.asUInt
      }
    }
    is(sEND) {
      stateSclk := sIDLE
      sclkReg := false.B
      countSclk := 0.asUInt
    }
  }

  // CSX
  switch(stateCsx) {
    is(sIDLE) {
      when(io.cbf) {
        rxReg := "b0000000000000000".U // clear buffer
        completed := false.B
      }.elsewhen(io.run) {
        stateCsx := sRUN
        rxReg := "b0000000000000000".U // clear buffer
        completed := false.B
        dcx := ~io.inDcx
        csxReg := false.B
        busy := true.B
      }
    }
    is(sRUN) {
      when(countCsx === 15.asUInt && ~lenOfData) { // length of data is 8bit
        csxReg := true.B
        dcx := false.B
        stateCsx := sEND
      }.elsewhen(countCsx === 31.asUInt && lenOfData) { // length of data is 16bit
        csxReg := true.B
        dcx := false.B
        stateCsx := sEND
      }.otherwise {
        countCsx := countCsx + 1.asUInt
      }
    }
    is(sEND) {
      stateCsx := sIDLE
      busy := false.B
      countCsx := 0.asUInt
    }
  }

  // MOSI and MISO
  switch(state) {
    is(sIDLE) {
      when(io.run) {
        state := sRUN
        lenOfData := io.lenOfData // set a length of data
        txReg := io.txData // set trasmitted data
      }
    }
    is(sRUN) {
      count := count + 1.asUInt
      when(count === 16.asUInt && ~lenOfData) {
        state := sEND
      }.elsewhen(count === 32.asUInt && lenOfData) {
        state := sEND
      }.elsewhen(count(0) === 1.asUInt) { // posedge of sclk
        when(lenOfData) { // length of data is 16bit
          txReg := txReg(14, 0) ## false.B
          rxBuff := rxBuff(14, 0) ## io.miso
        }.otherwise { // length of data is 8bit
          txReg := txReg(6, 0) ## false.B
          rxBuff := rxBuff(6, 0) ## io.miso
        }
      }
    }
    is(sEND) {
      state := sIDLE
      count := 0.asUInt
      lenOfData := false.B
      txReg := 0.asUInt
      rxReg := "b00000000".U ## rxBuff
    }
  }
}
