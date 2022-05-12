package ip.memory

import chisel3._
import chisel3.util.MuxCase

class SPRAM extends Module {
  val io = IO(new Bundle {
    val inM = Input(UInt(16.W))
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())

    val outM = Output(UInt(16.W))
  })

  val cs = MuxCase(
    "b0000".asUInt,
    Seq(
      (io.addrM(15, 14) === "b00".asUInt) -> "b0001".asUInt,
      (io.addrM(15, 14) === "b01".asUInt) -> "b0010".asUInt,
      (io.addrM(15, 14) === "b10".asUInt) -> "b0100".asUInt,
      (io.addrM(15, 14) === "b11".asUInt) -> "b1000".asUInt
    )
  )

  val spram00 = Module(new BlackBoxSPRAM())
  val spram01 = Module(new BlackBoxSPRAM())
  val spram10 = Module(new BlackBoxSPRAM())
  val spram11 = Module(new BlackBoxSPRAM())

  spram00.io.addr := io.addrM(13, 0)
  spram01.io.addr := io.addrM(13, 0)
  spram10.io.addr := io.addrM(13, 0)
  spram11.io.addr := io.addrM(13, 0)

  spram00.io.cs := cs(0)
  spram01.io.cs := cs(1)
  spram10.io.cs := cs(2)
  spram11.io.cs := cs(3)

  spram00.io.in := io.inM
  spram01.io.in := io.inM
  spram10.io.in := io.inM
  spram11.io.in := io.inM

  spram00.io.addr := io.addrM
  spram01.io.addr := io.addrM
  spram10.io.addr := io.addrM
  spram11.io.addr := io.addrM

  spram00.io.wren := io.writeM
  spram01.io.wren := io.writeM
  spram10.io.wren := io.writeM
  spram11.io.wren := io.writeM

  io.outM := MuxCase(
    0.asUInt,
    Seq(
      (io.addrM(15, 14) === "b00".asUInt) -> spram00.io.out,
      (io.addrM(15, 14) === "b01".asUInt) -> spram01.io.out,
      (io.addrM(15, 14) === "b10".asUInt) -> spram10.io.out,
      (io.addrM(15, 14) === "b11".asUInt) -> spram11.io.out
    )
  )

}
