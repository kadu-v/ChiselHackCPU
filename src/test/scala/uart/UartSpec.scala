package uart

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import top.Top
import chiseltest.WriteVcdAnnotation

class UartSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "Uart Rx(12 MHz, 115200 bps)"
  it should "recieve 0b01010101" in {
    test(new Rx(12, 115200)) { c =>
      // 104 clock
      c.io.rx.poke(true.B)
      c.clock.step(100)
      c.io.rx.poke(false.B) // start bit
      c.clock.step(104)
      c.io.rx.poke(1.B) // 1 bit 1
      c.clock.step(104)
      c.io.rx.poke(0.B) // 2 bit 0
      c.clock.step(104)
      c.io.rx.poke(1.B) // 3 bit 1
      c.clock.step(104)
      c.io.rx.poke(0.B) // 4 bit 0
      c.clock.step(104)
      c.io.rx.poke(1.B) // 5 bit 1
      c.clock.step(104)
      c.io.rx.poke(0.B) // 6 bit 0
      c.clock.step(104)
      c.io.rx.poke(1.B) // 7 bit 1
      c.clock.step(104)
      c.io.rx.poke(0.B) // 8 bit 0
      c.clock.step(104)
      c.io.rx.poke(true.B) // stop bit
      c.clock.step(104)
      c.io.dout.expect(0x55.asUInt()) // LSB
    }
  }

  it should "recieve 0b01010101, and write a mem[1024] = 0b01010101" in {
    test(new Top("./hack/tests/Uart1/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt())

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x55.asUInt())
      }
  }

  it should "recieve 0b01010101, and status and control register = b00000000000010" in {
    test(new Top("./hack/tests/Uart2/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt())

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x2.asUInt())
      }
  }

  it should "recieve 0b01010101, and status and control register = b00000000000001" in {
    test(new Top("./hack/tests/Uart3/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt())

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x01.asUInt())
      }
  }

  it should "recieve 0b01010101, and clear buffer" in {
    test(new Top("./hack/tests/Uart4/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.io.rx.poke(true.B)
        c.clock.step(100)
        c.io.rx.poke(false.B) // start bit
        c.clock.step(104)
        c.io.rx.poke(1.B) // 1 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 2 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 3 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 4 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 5 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 6 bit 0
        c.clock.step(104)
        c.io.rx.poke(1.B) // 7 bit 1
        c.clock.step(104)
        c.io.rx.poke(0.B) // 8 bit 0
        c.clock.step(104)
        c.io.rx.poke(true.B) // stop bit
        c.clock.step(104)
        // c.io.rxdebug.expect(0x55.asUInt())

        // wait here
        c.clock.step(1000)
        c.io.debug.expect(0x00.asUInt())
      }
  }

  behavior of "Uart Tx(12 MHz, 115200 bps)"
  it should "send 0b01010101" in {
    test(new Top("./hack/tests/Uart5/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.io.debug.expect(256.asUInt())
      }
  }

  it should "send 0b01010101, reset" in {
    test(new Top("./hack/tests/Uart5/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.reset.poke(true.B)
        c.clock.step(50)
        c.reset.poke(false.B)
        c.io.cts.poke(true.B)
        c.clock.step(5000)
        c.io.debug.expect(256.asUInt())
      }
  }
}
