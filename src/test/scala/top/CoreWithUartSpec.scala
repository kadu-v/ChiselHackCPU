package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation
import ip.interface.uart.{Rx, Tx}

class CoreWithUartSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {
  val words = 2048
  val freq = 25
  val boudRate = 115200

  class CoreWithUart(
      filename: String,
      init: String,
      words: Int,
      freq: Int,
      boudRate: Int
  ) extends Module {
    val io = IO(new Bundle {
      // Tx
      val din = Input(UInt(8.W))
      val run = Input(Bool())
      val busy = Output(Bool())

      // Rx

      // control flag
      val cbf = Input(Bool()) // clear buffer
      val dout = Output(UInt(16.W)) // recieved data

      val led0 = Output(UInt(16.W))
    })

    // clock: 100 MHz div4Clock: 25 MHz
    val core = Module(new Top(filename, init, words))
    // clock: 100 MHz
    val uartTx = Module(new Tx(100, boudRate))
    val uartRx = Module(new Rx(100, boudRate))

    core.io.rx := uartTx.io.tx
    uartTx.io.cts := core.io.rts

    core.io.rx := uartTx.io.tx
    uartTx.io.din := io.din
    uartTx.io.run := io.run
    io.busy := uartTx.io.busy

    core.io.cts := uartRx.io.rts
    uartRx.io.rx := core.io.tx
    uartRx.io.cbf := io.cbf
    io.dout := uartRx.io.dout

    core.io.miso := false.B

    io.led0 := core.io.led0
  }

  behavior of "Uart Rx(25 MHz, 115200 bps)"
  it should "recieve 0b01010101, and write a mem[1024] = 0b01010101" in {
    test(
      new CoreWithUart(
        "./hack/tests/Uart1/jack.hack",
        "./hack/init.bin",
        words,
        freq,
        boudRate
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.din.poke("b01010101".U(8.W))
        c.clock.step(100)
        c.io.run.poke(true.B)
        c.clock.step(10)
        c.io.run.poke(false.B)

        c.clock.step(15000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "recieve 0b01010101, and status and control register = b00000000000010" in {
    test(
      new CoreWithUart(
        "./hack/tests/Uart2/jack.hack",
        "./hack/init.bin",
        words,
        freq,
        boudRate
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.din.poke("b01010101".U(8.W))
        c.clock.step(100)
        c.io.run.poke(true.B)
        c.clock.step(10)
        c.io.run.poke(false.B)

        c.clock.step(25000)
        c.io.led0.expect(1.asUInt)
      }
  }

  it should "recieve 0b01010101 and 0b10101010, " in {
    test(
      new CoreWithUart(
        "./hack/tests/Uart3/jack.hack",
        "./hack/init.bin",
        words,
        freq,
        boudRate
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.din.poke(10)
        c.clock.step(100)
        c.io.run.poke(true.B)
        c.clock.step(10)
        c.io.run.poke(false.B)
        c.clock.step(25000)

        c.io.din.poke(20)
        c.clock.step(100)
        c.io.run.poke(true.B)
        c.clock.step(10)
        c.io.run.poke(false.B)
        c.clock.step(25000)

        c.clock.step(1000)
        c.io.led0.expect(1.asUInt)
      }
  }

  behavior of "Uart Tx(25 MHz, 115200 bps)"
  it should "send 81" in {
    test(
      new CoreWithUart(
        "./hack/tests/Uart5/jack.hack",
        "./hack/init.bin",
        words,
        freq,
        boudRate
      )
    )
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(25000)
        c.io.dout.expect(81)
        c.io.led0.expect(1.asUInt)
      }
  }

  behavior of "Uart Tx and Rx(25 MHz, 115200 bps)"
  it should "receive a character and send the same character" in {
    test(new CoreWithUart("./hack/tests/Uart6/jack.hack", "./hack/init.bin", words, freq, boudRate))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c => 
        c.clock.setTimeout(0)
        c.io.din.poke(98)
        c.clock.step(100)
        c.io.run.poke(true.B)
        c.clock.step(10)
        c.io.run.poke(false.B)
        c.clock.step(25000)

        c.io.dout.expect(98)
        c.io.led0.expect(1.asUInt)
    }
  }

  behavior of "Test"
  it should "Test/bin.hack" in {
    test(new CoreWithUart("./hack/tests/Tests/bin.hack", "./hack/init.bin", words, freq, boudRate))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c => 
        c.clock.setTimeout(0)
        c.clock.step(25000)

        c.io.dout.expect(98)
        c.io.led0.expect(1.asUInt)
    }
  }
}
