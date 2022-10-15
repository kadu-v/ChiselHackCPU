package ip.memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit
import firrtl.annotations.MemoryLoadFileType

class EBRAM(init: String) extends Module {
  val io = IO(new Bundle {
    val inM = Input(UInt(16.W))
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())

    // Output
    val outM = Output(UInt(16.W))
  })

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(1024, UInt(16.W))
  when(io.writeM) {
    mem(io.addrM) := io.inM
  }
  loadMemoryFromFileInline(mem, init, MemoryLoadFileType.Binary)

  io.outM := mem(io.addrM(12, 0))
}
