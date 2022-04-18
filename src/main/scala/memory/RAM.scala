package memory

import chisel3._
import javax.xml.transform.OutputKeys

class RAM extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val addr = Input(UInt(16.W))
    val write_m = Input(Bool())

    // Output
    val out = Output(UInt(16.W))

    // Debug
    val debug = Output(UInt(16.W))
    val debug2 = Output(UInt(16.W))
  })

  val mem = Mem(2048, UInt(16.W))
  when(io.write_m) {
    mem(io.addr) := io.in
  }

  io.out := mem(io.addr(10, 0))

  // Debug signal
  io.debug := mem(1024)
  io.debug2 := mem(0)
}
