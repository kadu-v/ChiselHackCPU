package ip.memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit
import firrtl.annotations.MemoryLoadFileType

/** document about initialization of memory
  * https://www.chisel-lang.org/chisel3/docs/appendix/experimental-features#loading-memories
  */

class EBROM(file: String, words: Int) extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(16.W))

    val out = Output(UInt(16.W))
  })

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(words, UInt(16.W))
  loadMemoryFromFileInline(mem, file, MemoryLoadFileType.Binary)
  io.out := mem(io.addr(10, 0))
}
