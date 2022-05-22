package ip.led
import chisel3._
import chisel3.util._

/*
https://digilent.com/reference/pmod/pmodssd/reference-manual
 */

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
  val divider = (freq * 1000000 / 500).asUInt // 240 Hz
  val cnt = RegInit(0.U(16.W))

  when(io.addrM === ledRegAddr.asUInt && io.writeM) {
    seg1 := decode(io.inM(3, 0))
    seg2 := decode(io.inM(7, 4))
  }

  when(cnt === divider) {
    cs := ~cs
    cnt := 0.asUInt
  }.otherwise {
    cnt := cnt + 1.asUInt
  }

  /* connect IO */
  io.outM := 0.asUInt
  io.out := Mux(cs, seg2, seg1)
  io.cs := cs

  // decode 8bit to 7 segment
  def decode(seg: UInt): UInt = {
    MuxCase(
      0x08.asUInt,
      Seq(
        (seg === 0.asUInt) -> 0x7e.U(7.W), // 0
        (seg === 1.asUInt) -> 0x30.U(7.W), // 1
        (seg === 2.asUInt) -> 0x6d.U(7.W), // 2
        (seg === 3.asUInt) -> 0x79.U(7.W), // 3
        (seg === 4.asUInt) -> 0x33.U(7.W), // 4
        (seg === 5.asUInt) -> 0x5b.U(7.W), // 5
        (seg === 6.asUInt) -> 0x5f.U(7.W), // 6
        (seg === 7.asUInt) -> 0x70.U(7.W), // 7
        (seg === 7.asUInt) -> 0x7f.U(7.W), // 8
        (seg === 9.asUInt) -> 0x7b.U(7.W), // 9
        (seg === 10.asUInt) -> 0x7e.U(7.W), // A
        (seg === 11.asUInt) -> 0x1f.U(7.W), // b
        (seg === 12.asUInt) -> 0x4e.U(7.W), // C
        (seg === 13.asUInt) -> 0x3d.U(7.W), // d
        (seg === 14.asUInt) -> 0x4f.U(7.W), // E
        (seg === 15.asUInt) -> 0x47.U(7.W) // F
      )
    )
  }
}
