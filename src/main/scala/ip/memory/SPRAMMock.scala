package ip.memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit
import firrtl.annotations.MemoryLoadFileType

class SPRAMMock extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val addr = Input(UInt(16.W))
    val writeM = Input(Bool())

    val out = Output(UInt(16.W))
  })

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(65536, UInt(16.W))
  when(io.writeM) {
    mem(io.addr) := io.in
  }
  // loadMemoryFromFileInline(mem, init, MemoryLoadFileType.Binary)

  io.out := mem(io.addr)
}
