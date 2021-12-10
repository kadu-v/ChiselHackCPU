package hackcore

import chisel3._

class Core extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(16.W))
    val in_m = Input(UInt(16.W))

    val out_m = Output(UInt(16.W))
    val write_m = Output(Bool())
    val addr_m = Output(UInt(16.W))
    val pc = Output(UInt(16.W))
  })

  // decode
  val decode = Module(new Decode)

  // A register
  val reg_a = Module(new Register)

  // D registe
  val reg_d = Module(new Register)

  // MUX 1
  val mux1 = Module(new MUX)

  // MUX 2
  val mux2 = Module(new MUX)

  // ALU
  val alu = Module(new ALU)

  // PCLoad
  val pc_load = Module(new PCLoad)

  // PC
  val pc = Module(new PC)

  // decode
  decode.io.inst := io.inst

  // MUX 1
  mux1.io.in1 := io.inst
  mux1.io.in2 := io.out_m
  mux1.io.sel := decode.io.mux_sel1

  // A register
  reg_a.io.in := mux1.io.out
  reg_a.io.load := decode.io.load_a

  // MUX 2
  mux2.io.in1 := reg_a.io.out
  mux2.io.in2 := io.in_m
  mux2.io.sel := decode.io.mux_sel2

  // D register
  reg_d.io.in := io.out_m
  reg_d.io.load := decode.io.load_d

  // ALU
  alu.io.in1 := reg_d.io.out
  alu.io.in2 := mux2.io.out
  alu.io.zx := decode.io.zx
  alu.io.nx := decode.io.nx
  alu.io.zy := decode.io.zy
  alu.io.ny := decode.io.ny
  alu.io.f := decode.io.f
  alu.io.no := decode.io.no

  // PC Load
  pc_load.io.zr := alu.io.zr
  pc_load.io.ng := alu.io.ng
  pc_load.io.j1 := decode.io.j1
  pc_load.io.j2 := decode.io.j2
  pc_load.io.j3 := decode.io.j3
  pc_load.io.pc_flag := decode.io.pc_flag

  // PC
  pc.io.load := pc_load.io.load_pc
  pc.io.inc := true.B
  pc.io.a := reg_a.io.out

  io.out_m := alu.io.out
  io.write_m := decode.io.write_m
  io.addr_m := reg_a.io.out
  io.pc := pc.io.out

}
