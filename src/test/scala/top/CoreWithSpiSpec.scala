package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class CoreWithSpiSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {
  val romWords = 2048 + 1024
  val ramWords = 1024
  val freq = 25

  class CoreWithSpi(
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

  behavior of "SPI Master"
  it should "send initial commands" in {
    test(
      new CoreWithSpi(
        "./hack/tests/Spi1/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(50000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "send clear an initial screen" in {
    test(
      new CoreWithSpi(
        "./hack/tests/Spi2/bin.hack",
        "./hack/init.bin",
        romWords,
        ramWords
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(60000)
        c.io.led0.expect(1.asUInt)
      }
  }
}
