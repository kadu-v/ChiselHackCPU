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
  val words = 2048
  val freq = 25
  behavior of "SPI Master"
  it should "send initial commands" in {
    test(new Top("./hack/tests/Spi1/jack.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(200000)
        c.io.led0.expect(1.asUInt)
      }
  }
}
