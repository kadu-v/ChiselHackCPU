package memory

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class ROM(file: String = "") extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(16.W))

    val out = Output(UInt(16.W))
  })

  val mem = Mem(16384, UInt(16.W))
  loadMemoryFromFile(mem, file)
  io.out := mem(io.addr)
}
