package ip.memory

import chisel3._
import chisel3.util.experimental.{loadMemoryFromFileInline}
import chisel3.experimental.{annotate, ChiselAnnotation}
import firrtl.annotations.MemorySynthInit
import firrtl.annotations.MemoryLoadFileType

class SPRAM256KMock extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val addr = Input(UInt(13.W))
    val wren = Input(Bool())
    val cs = Input(Bool()) // H: active, L: inactive

    val out = Output(UInt(16.W))
  })

  annotate(new ChiselAnnotation {
    override def toFirrtl = MemorySynthInit
  })

  val mem = Mem(8192, UInt(16.W))
  when(io.wren && io.cs) {
    mem(io.addr) := io.in
  }

  io.out := Mux(io.cs, mem(io.addr), 0.asUInt)

}
