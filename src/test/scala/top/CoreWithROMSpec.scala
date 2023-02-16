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
  behavior of "ROM"
  it should "switch from EBROM to SROM" in {
    test(
      new Top(
        "./hack/tests/SROM/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(40000)
        c.io.led0.expect(1.asUInt)
      }
  }
}
