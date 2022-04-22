package uart

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import top.Top
import chiseltest.WriteVcdAnnotation

class UartRxSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
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

  it should "recieve 0b01010101 and write a mem[1024]" in {
    test(new Top("./hack/tests/Uart1/vm.hack", "./hack/init.bin"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
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
}
