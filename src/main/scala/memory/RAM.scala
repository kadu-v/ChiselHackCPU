package memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit
import firrtl.annotations.MemoryLoadFileType

class RAM(init: String) extends Module {
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

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(2048, UInt(16.W))
  when(io.write_m) {
    mem(io.addr) := io.in
  }
  loadMemoryFromFileInline(mem, init, MemoryLoadFileType.Binary)

  io.out := mem(io.addr(10, 0))

  // Debug signal
  io.debug := mem(1024)
  io.debug2 := mem(0)
}
