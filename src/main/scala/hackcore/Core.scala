package hackcore

import chisel3._

class Core extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(16.W))
    val inM = Input(UInt(16.W))

    val outM = Output(UInt(16.W))
    val writeM = Output(Bool())
    val addrM = Output(UInt(16.W))
    val pc = Output(UInt(16.W))
  })

  // decode
  val decode = Module(new Decode)

  // A register
  val regA = Module(new Register)

  // D registe
  val regD = Module(new Register)

  // MUX 1
  val mux1 = Module(new MUX)

  // MUX 2
  val mux2 = Module(new MUX)

  // ALU
  val alu = Module(new ALU)

  // PCLoad
  val pcLoad = Module(new PCLoad)

  // PC
  val pc = Module(new PC)

  // decode
  decode.io.inst := io.inst

  // MUX 1
  mux1.io.in1 := io.inst
  mux1.io.in2 := io.outM
  mux1.io.sel := decode.io.muxSel1

  // A register
  regA.io.in := mux1.io.out
  regA.io.load := decode.io.loadA

  // MUX 2
  mux2.io.in1 := regA.io.out
  mux2.io.in2 := io.inM
  mux2.io.sel := decode.io.muxSel2

  // D register
  regD.io.in := io.outM
  regD.io.load := decode.io.loadD

  // ALU
  alu.io.in1 := regD.io.out
  alu.io.in2 := mux2.io.out
  alu.io.zx := decode.io.zx
  alu.io.nx := decode.io.nx
  alu.io.zy := decode.io.zy
  alu.io.ny := decode.io.ny
  alu.io.f := decode.io.f
  alu.io.no := decode.io.no

  // PC Load
  pcLoad.io.zr := alu.io.zr
  pcLoad.io.ng := alu.io.ng
  pcLoad.io.j1 := decode.io.j1
  pcLoad.io.j2 := decode.io.j2
  pcLoad.io.j3 := decode.io.j3
  pcLoad.io.pcFlag := decode.io.pcFlag

  // PC
  pc.io.load := pcLoad.io.loadPC
  pc.io.inc := true.B
  pc.io.a := regA.io.out

  io.outM := alu.io.out
  io.writeM := decode.io.writeM
  io.addrM := regA.io.out
  io.pc := pc.io.out

}
