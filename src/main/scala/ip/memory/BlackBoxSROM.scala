package ip.memory

import chisel3._
import chisel3.experimental.Analog
import chisel3.util.HasBlackBoxInline

class BlackBoxSROM extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val inM = Input(UInt(16.W))
    val outM = Output(UInt(16.W))
    val writeM = Input(Bool())

    val pin = Analog(16.W)
  })

    setInline(
    "BlackBoxSROM.v",
    """
      module BlackBoxSROM(
      |   input   wire  [15:0]  in,
      |   output  wire  [15:0]  out,
      |   input   wire          write,
      |   inout   wire  [15:0]  pin 
      |);
      |
      |// handle a inout pin
      |assign pin = (write) ? in : 16'bzzzzzzzzzzzzzzzz;
      |assign out = pin;
      |endmodule
    """.stripMargin
  )
}