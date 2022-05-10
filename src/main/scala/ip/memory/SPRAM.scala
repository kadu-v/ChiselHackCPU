package ip.memory

import chisel3._
import chisel3.util.MuxCase

class SPRAM extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val addr = Input(UInt(16.W))
    val writeM = Input(Bool())

    val out = Output(UInt(16.W))
  })

  val cs = MuxCase(
    "b0000".asUInt,
    Seq(
      (io.addr(15, 14) === "b00".asUInt) -> "b0001".asUInt,
      (io.addr(15, 14) === "b01".asUInt) -> "b0010".asUInt,
      (io.addr(15, 14) === "b10".asUInt) -> "b0100".asUInt,
      (io.addr(15, 14) === "b11".asUInt) -> "b1000".asUInt
    )
  )

  val spram00 = Module(new BlackBoxSPRAM())
  val spram01 = Module(new BlackBoxSPRAM())
  val spram10 = Module(new BlackBoxSPRAM())
  val spram11 = Module(new BlackBoxSPRAM())

  spram00.io.addr := io.addr(13, 0)
  spram01.io.addr := io.addr(13, 0)
  spram10.io.addr := io.addr(13, 0)
  spram11.io.addr := io.addr(13, 0)

  spram00.io.cs := cs(0)
  spram01.io.cs := cs(1)
  spram10.io.cs := cs(2)
  spram11.io.cs := cs(3)

  spram00.io.in := io.in
  spram01.io.in := io.in
  spram10.io.in := io.in
  spram11.io.in := io.in

  spram00.io.addr := io.addr
  spram01.io.addr := io.addr
  spram10.io.addr := io.addr
  spram11.io.addr := io.addr

  spram00.io.wren := io.writeM
  spram01.io.wren := io.writeM
  spram10.io.wren := io.writeM
  spram11.io.wren := io.writeM

  val out = MuxCase(
    0.asUInt,
    Seq(
      (io.addr(15, 14) === "b00".asUInt) -> spram00.io.out,
      (io.addr(15, 14) === "b01".asUInt) -> spram01.io.out,
      (io.addr(15, 14) === "b10".asUInt) -> spram10.io.out,
      (io.addr(15, 14) === "b11".asUInt) -> spram11.io.out
    )
  )

  io.out := out

}
