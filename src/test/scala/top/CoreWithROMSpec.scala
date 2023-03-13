package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class CoreWithSROMSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {
  val romWords = 2048 + 1024
  val ramWords = 1024
  val freq = 25

  class CoreWithSROM(
      filename: String,
      init: String,
      romWords: Int,
      ramWords: Int
  ) extends Module {
    val io = IO(new Bundle {
      val led0 = Output(UInt(16.W))

      val dummy = Output(UInt(16.W))
    })

    val core = Module(new Top(filename, init, romWords, ramWords, true))

    io.led0 := core.io.led0

    // To determine the size of an analog wire
    core.io.rx := RegInit(0.U(8.W))
    io.dummy := core.io.tx

    // To fully initialize I/O
    core.io.cts := true.B
    core.io.miso := false.B
  }

  behavior of "ROM"
  it should "switch from EBROM to SROM" in {
    test(
      new CoreWithSROM(
        "./hack/tests/SROM/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(40000)
        c.io.led0.expect(1.asUInt)
      }
  }
}
