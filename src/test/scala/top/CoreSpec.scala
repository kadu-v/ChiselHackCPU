package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class CoreSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val words = 2048
  behavior of "Hack Core"
  it should "push constants" in {
    test(new Top("./hack/tests/Const/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(2000)
        c.io.debug.expect(15.asUInt)
      }
  }

  it should "add (8 + 8 = 16)" in {
    test(new Top("./hack/tests/Add/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(2000)
        c.io.debug.expect(16.asUInt)
      }
  }

  it should "sub (8 - 7 = 1)" in {
    test(new Top("./hack/tests/Sub/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(2000)
        c.io.debug.expect(1.asUInt)
      }
  }

  it should "do fib(2) = 1 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib2/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(2000)
        c.io.debug.expect(1.asUInt)
      }
  }

  it should "do fib(6) = 8 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib6/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(32000)
        c.io.debug.expect(8.asUInt)
      }
  }

  it should "switch instruction's memory from EBRAM to SPRAM" in {
    test(new Top("./hack/tests/SPRAM1/vm.hack", "./hack/init.bin", words))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(5000)
        c.io.debug.expect(1.asUInt)

      }
  }
}
