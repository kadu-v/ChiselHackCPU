package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class CoreSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val romWords = 2048
  val ramWords = 2048

  class Core(
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

  behavior of "Hack Core"
  it should "push constants" in {
    test(
      new Core(
        "./hack/tests/Const/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
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
      new Core(
        "./hack/tests/Add/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
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
      new Core(
        "./hack/tests/Sub/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
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
      new Core(
        "./hack/tests/Fib2/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
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
      new Core(
        "./hack/tests/Fib6/jack.hack",
        "./hack/init.bin",
        romWords,
        ramWords
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(20000)
        c.io.led0.expect(1.asUInt)
      }
  }

  // it should "loop 100 times" in {
  //   test(
  //     new Core(
  //       "./hack/tests/Loop/jack.hack",
  //       "./hack/init.bin",
  //       romWords,
  //       ramWords
  //     )
  //   )
  //     .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
  //       c.clock.setTimeout(0)
  //       c.clock.step(55000)
  //       c.io.led0.expect(1.asUInt)

  //     }
  // }
}
