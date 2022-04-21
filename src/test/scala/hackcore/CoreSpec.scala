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
    test(new Top("./hack/tests/Const/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(15.asUInt())
      }
  }

  it should "test sadd" in {
    test(new Top("./hack/tests/sadd.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug2.expect(5.asUInt())
      }
  }

  it should "test add (8 + 8 = 16)" in {
    test(new Top("./hack/tests/Add/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(16.asUInt())
      }
  }

  it should "test sub (8 - 7 = 1)" in {
    test(new Top("./hack/tests/Sub/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.step(500)
        c.io.debug.expect(1.asUInt())
      }
  }

  it should "test fib(2) = 1 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib2/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(1000)
        c.io.debug.expect(1.asUInt())
      }
  }

  it should "test fib(6) = 8 (fib(0) = 0, fib(1) = 1)" in {
    test(new Top("./hack/tests/Fib6/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(5000)
        c.io.debug.expect(8.asUInt())
      }
  }
}
