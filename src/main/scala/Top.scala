package top

import hackcore.Core
import memory.{RAM, ROM}
import chisel3._

class Top extends Module {
  val io = IO(new Bundle {
    val GPIO = Output(Bool())
  })

  // Hack CPU core
  val core = Module(new Core())

  // RAM
  val ram = Module(new RAM)

  // ROM
  val rom = Module(new ROM("hack/sample.hex"))

  // core
  core.io.inst := rom.io.out
  core.io.in_m := ram.io.out

  // ram
  ram.io.in := core.io.out_m
  ram.io.addr := core.io.addr_m
  ram.io.write_en := core.io.write_m

  // rom
  rom.io.addr := core.io.pc

  io.GPIO := core.io.write_m

}

object Elaborate extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Top(), args)
}
