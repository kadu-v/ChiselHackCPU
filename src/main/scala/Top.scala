package top

import hackcore.Core
import memory.ROM

import chisel3._
import mmio.MMIO

class Top(filename: String) extends Module {
  val io = IO(new Bundle {
    val GPIO = Output(Bool())
  })

  // Hack CPU core
  val core = Module(new Core())

  // RAM
  val mem = Module(new MMIO())

  // ROM
  val rom = Module(new ROM(filename))

  // core
  core.io.inst := rom.io.out
  core.io.in_m := mem.io.out

  // Memory mapped IO
  mem.io.in_m := core.io.out_m
  mem.io.addr_m := core.io.addr_m
  mem.io.write_m := core.io.write_m
  mem.io.rx := io.GPIO

  // rom
  rom.io.addr := core.io.pc

  io.GPIO := core.io.write_m

}

object Elaborate extends App {
  val argsx = args :+ "--target-dir" :+ "out"
  (new chisel3.stage.ChiselStage).emitVerilog(new Top("./hack/bin.hack"), argsx)
}
