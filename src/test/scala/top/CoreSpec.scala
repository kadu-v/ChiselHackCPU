package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class CoreSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val romWords = 2048
  val ramWords = 2048
  behavior of "Hack Core"
  it should "push constants" in {
    test(
      new Top(
        "./hack/tests/Const/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(4000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "add (1 + 9 = 10)" in {
    test(
      new Top(
        "./hack/tests/Add/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(4000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "sub (1 - 100 = -91)" in {
    test(
      new Top(
        "./hack/tests/Sub/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(4000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "do fib(2) = 2 (fib(0) = 1, fib(1) = 1)" in {
    test(
      new Top(
        "./hack/tests/Fib2/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(10000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "do fib(6) = 8 (fib(0) = 0, fib(1) = 1)" in {
    test(
      new Top(
        "./hack/tests/Fib6/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords,
        true
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(20000)
        c.io.led0.expect(1.asUInt)
      }
  }

  // it should "switch instruction's memory from EBRAM to SPRAM" in {
  //   test(new Top("./hack/tests/SPRAM1/vm.hack", "./hack/init.bin", romWords, ramWords, true))
  //     .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
  //       c.clock.setTimeout(0)
  //       c.clock.step(5000)
  //       c.io.led0.expect(1.asUInt)

  //     }
  // }
}
