import top._

object Elaborater extends App {
  val argsx =
    args :+ "--target-dir" :+ "apio" :+ "--emission-options=disableMemRandomization,disableRegisterRandomization"
  (new chisel3.stage.ChiselStage)
    .emitVerilog(
      new Top("bin.hack", "init.bin", 256, 4096 - 256, false),
      argsx
    )
}
