package hackcore

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import top.Top
import chiseltest.WriteVcdAnnotation

class HackCoreSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {
  behavior of "HackCPU"

  it should "test constants" in {
    test(new Top("./hack/tests/const.hack"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(15.asUInt())
      }
  }

  it should "test sadd" in {
    test(new Top("./hack/tests/sadd.hack"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug2.expect(5.asUInt())
      }
  }

  it should "test add" in {
    test(new Top("./hack/tests/add.hack"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(16.asUInt())
      }
  }
}
