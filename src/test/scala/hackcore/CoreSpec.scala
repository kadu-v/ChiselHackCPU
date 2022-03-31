package hackcore

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import top.Top

class HackCoreSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {
  behavior of "HackCPU"
  it should "test bin.hex" in {
    test(new Top("./hack/bin.hack")) { c =>
      c.clock.step(500)
    }
  }
}
