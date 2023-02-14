package ip.memory

import chisel3._
import chisel3.experimental.Analog
import chisel3.util.HasBlackBoxInline

class BlackBoxSROM extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val inM = Input(UInt(16.W))
    val outM = Output(UInt(16.W))
    val writeM = Input(Bool())
    val addrM = Input(UInt(18.W))

    // val DATA = Analog(16.W)
  })

  setInline(
    "BlackBoxSROM.v",
    """
      |module BlackBoxSROM(
      |   input                 clock,
      |   input   wire          writeM,
      |   input   wire  [15:0]  inM,
      |   output  wire  [15:0]  outM,
      |   input   wire  [17:0]  addrM,
      |   inout   wire  [15:0]  SRAM_DATA,	 // SRAM data 16 Bit
      |   output  wire  [17:0]  SRAM_ADDR, 	 // SRAM addr 18 Bit
      |   output                SRAM_CSX, 	 // SRAM chip_enable_not
      |   output                SRAM_OEX,    // SRAM output_enable_not
      |   output                SRAM_WEX		 // SRAM write_enable_not
      |);
      |reg [15:0] inner = 0;
      |reg inner_csx = 0;
      |reg inner_oex = 0;
      |reg inner_wex = 1;
      |
      |always @(posedge clock) begin
      |   inner <= inM;
      |   if (writeM) begin
      |     inner_oex <= 1;
      |     inner_wex <= 0;
      |   end else begin
      |     inner_oex <= 0;
      |     inner_wex <= 1;
      |   end
      |end
      |
      |// handle a inout pin
      |assign SRAM_DATA = (~inner_wex) ? inner : 16'bzzzzzzzzzzzzzzzz;
      |
      |assign outM = SRAM_DATA;
      |assign SRAM_ADDR = addrM;
      |assign SRAM_CSX = inner_csx;
      |assign SRAM_OEX = inner_oex;
      |assign SRAM_WEX = inner_wex;
      |endmodule
    """.stripMargin
  )
}
