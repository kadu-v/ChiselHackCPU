package memory

import chisel3._

class RAM extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val addr = Input(UInt(16.W))
    val write_m = Input(Bool())

    val out = Output(UInt(16.W))
  })

  val mem = Mem(2048, UInt(16.W))
  when(io.write_m) {
    mem(io.addr) := io.in
  }
  io.out := mem(io.addr(10, 0))

}
