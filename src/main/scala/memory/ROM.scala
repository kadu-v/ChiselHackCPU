package memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline, loadMemoryFromFile}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit

class ROM(file: String = "") extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(16.W))

    val out = Output(UInt(16.W))
  })

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(16384, UInt(16.W))
  loadMemoryFromFileInline(mem, file)
  io.out := mem(io.addr)
}
