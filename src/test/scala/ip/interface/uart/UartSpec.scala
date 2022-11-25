package ip.interface.uart

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import top.Top
import chiseltest.WriteVcdAnnotation

class UartSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val clockStep = 25 * 1000000 / 115200
  behavior of "Uart Rx(25 MHz, 115200 bps)"
  it should "recieve 0b01010101" in {
    test(new Rx(25, 115200)) { c =>
      // 104 clock
      c.io.rx.poke(true.B)
      c.clock.step(100)
      c.io.rx.poke(false.B) // start bit
      c.clock.step(clockStep)
      c.io.rx.poke(1.B) // 1 bit 1
      c.clock.step(clockStep)
      c.io.rx.poke(0.B) // 2 bit 0
      c.clock.step(clockStep)
      c.io.rx.poke(1.B) // 3 bit 1
      c.clock.step(clockStep)
      c.io.rx.poke(0.B) // 4 bit 0
      c.clock.step(clockStep)
      c.io.rx.poke(1.B) // 5 bit 1
      c.clock.step(clockStep)
      c.io.rx.poke(0.B) // 6 bit 0
      c.clock.step(clockStep)
      c.io.rx.poke(1.B) // 7 bit 1
      c.clock.step(clockStep)
      c.io.rx.poke(0.B) // 8 bit 0
      c.clock.step(clockStep)
      c.io.rx.poke(true.B) // stop bit
      c.clock.step(clockStep)
      c.io.dout.expect(0x55.asUInt) // LSB
    }
  }

}
