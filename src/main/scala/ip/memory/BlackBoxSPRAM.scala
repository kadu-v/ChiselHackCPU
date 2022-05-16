package ip.memory

import chisel3._
import chisel3.util.HasBlackBoxInline

class BlackBoxSPRAM extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val cs = Input(Bool()) // H: active L: inactive
    val in = Input(UInt(16.W))
    val addr = Input(UInt(14.W))
    val wren = Input(Bool())

    val out = Output(UInt(16.W))
  })

  setInline(
    "BlackBoxSPRAM.v",
    """module BlackBoxSPRAM(
      |   input   wire          clock,
      |   input   wire          cs,
      |   input   wire  [15:0]  in,
      |   input   wire  [13:0]  addr,
      |   input   wire          wren,
      |   output  wire  [15:0]  out
      |);
      |SB_SPRAM256KA spram
      |  (
      |    .ADDRESS(      addr      ),
      |    .DATAIN(       in        ),
      |    .MASKWREN(     4'b1111   ),
      |    .WREN(         wren      ),
      |    .CHIPSELECT(   cs        ),
      |    .CLOCK(        clock     ),
      |    .STANDBY(      1'b0      ),
      |    .SLEEP(        1'b0      ),
      |    .POWEROFF(     1'b1      ),
      |    .DATAOUT(      out       )
      |  );
      |endmodule
    """.stripMargin
  )
}
