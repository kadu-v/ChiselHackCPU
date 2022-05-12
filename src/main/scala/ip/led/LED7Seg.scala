package ip.led
import chisel3._
import chisel3.util._

class LED7Seg(
    freq: Int // MHz
) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))

    val outSeg1 = Output(UInt(7.W))
    val outSeg2 = Output(UInt(7.W))
    val csSeg = Output(Bool()) // H: Seg1, L: Seg2
  })

  val seg1 = RegInit(0.asUInt)
  val seg2 = RegInit(0.asUInt)
  val cs = RegInit(false.B)
  val divider = (freq * 100000 / 240).asUInt // 240 Hz
  val cnt = RegInit(0.asUInt)

  seg1 := decode(io.in(7, 0))
  seg1 := decode(io.in(15, 8))

  when(cnt === divider) {
    cs := ~cs
    divider := 0.asUInt
  }.otherwise {
    divider := divider + 1.asUInt
  }

  /* connect IO */
  io.outSeg1 := seg1
  io.outSeg2 := seg2
  io.csSeg := cs

  // decode 8bit to 7 segment
  def decode(seg: UInt): UInt = {
    MuxCase(
      0x77.asUInt,
      Seq(
        (io.in === 0.asUInt) -> 0x7e.U(8.W),
        (io.in === 1.asUInt) -> 0x30.U(8.W),
        (io.in === 2.asUInt) -> 0x6d.U(8.W),
        (io.in === 3.asUInt) -> 0x79.U(8.W),
        (io.in === 4.asUInt) -> 0x33.U(8.W),
        (io.in === 5.asUInt) -> 0x5b.U(8.W),
        (io.in === 6.asUInt) -> 0x5f.U(8.W),
        (io.in === 7.asUInt) -> 0x70.U(8.W),
        (io.in === 8.asUInt) -> 0x7f.U(8.W),
        (io.in === 9.asUInt) -> 0x7b.U(8.W),
        (io.in === 10.asUInt) -> 0x00.U(8.W),
        (io.in === 11.asUInt) -> 0x00.U(8.W),
        (io.in === 12.asUInt) -> 0x00.U(8.W),
        (io.in === 13.asUInt) -> 0x00.U(8.W),
        (io.in === 14.asUInt) -> 0x00.U(8.W),
        (io.in === 15.asUInt) -> 0x00.U(8.W)
      )
    )
  }
}
