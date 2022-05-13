package ip.led
import chisel3._
import chisel3.util._

class LED7Seg(
    freq: Int, // MHz
    ledRegAddr: Int
) extends Module {
  val io = IO(new Bundle {
    val addrM = Input(UInt(16.W))
    val writeM = Input(Bool())
    val inM = Input(UInt(16.W))
    val outM = Output(UInt(16.W))

    val out = Output(UInt(7.W)) // A, B, C, D, E, F, G
    val cs = Output(Bool()) // H: Seg1, L: Seg2
  })

  val seg1 = RegInit(0.asUInt)
  val seg2 = RegInit(0.asUInt)
  val cs = RegInit(false.B)
  val divider = (freq * 100000 / 240).asUInt // 240 Hz
  val cnt = RegInit(0.asUInt)

  when(io.addrM === ledRegAddr.asUInt && io.writeM) {
    seg1 := decode(io.inM(7, 0))
    seg2 := decode(io.inM(15, 7))
  }

  when(cnt === divider) {
    cs := ~cs
    divider := 0.asUInt
  }.otherwise {
    divider := divider + 1.asUInt
  }

  /* connect IO */
  io.out := Mux(cs, seg1, seg2)
  io.cs := cs

  // decode 8bit to 7 segment
  def decode(seg: UInt): UInt = {
    MuxCase(
      0x77.asUInt,
      Seq(
        (io.inM === 0.asUInt) -> 0x7e.U(7.W), // 0
        (io.inM === 1.asUInt) -> 0x30.U(7.W), // 1
        (io.inM === 2.asUInt) -> 0x6d.U(7.W), // 2
        (io.inM === 3.asUInt) -> 0x79.U(7.W), // 3
        (io.inM === 4.asUInt) -> 0x33.U(7.W), // 4
        (io.inM === 5.asUInt) -> 0x5b.U(7.W), // 5
        (io.inM === 6.asUInt) -> 0x5f.U(7.W), // 6
        (io.inM === 7.asUInt) -> 0x70.U(7.W), // 7
        (io.inM === 7.asUInt) -> 0x7f.U(7.W), // 8
        (io.inM === 9.asUInt) -> 0x7b.U(7.W), // 9
        (io.inM === 10.asUInt) -> "b1110110".U(7.W), // A
        (io.inM === 11.asUInt) -> "b0011111".U(7.W), // b
        (io.inM === 12.asUInt) -> "b1000110".U(7.W), // C
        (io.inM === 13.asUInt) -> "b0111101".U(7.W), // d
        (io.inM === 14.asUInt) -> "b1001111".U(7.W), // E
        (io.inM === 15.asUInt) -> "b1110111".U(7.W) // F
      )
    )
  }
}
