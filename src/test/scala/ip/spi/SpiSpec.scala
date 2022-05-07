package ip.spi

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class SpiSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "SPI Master"
  it should "transmit 0b01010101" in {
    test(new Master()).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      c.clock.step(4)
      c.io.txData.poke("b01010101".asUInt())
      c.io.miso.poke(false.B)
      c.io.run.poke(true.B)
      c.clock.step(1)
      c.io.mosi.expect(false.B)
      c.clock.step(2)
      c.io.mosi.expect(true.B)
      c.clock.step(2)
      c.io.mosi.expect(false.B)
      c.clock.step(2)
      c.io.mosi.expect(true.B)
      c.clock.step(2)
      c.io.mosi.expect(false.B)
      c.clock.step(2)
      c.io.mosi.expect(true.B)
      c.clock.step(2)
      c.io.mosi.expect(false.B)
      c.clock.step(2)
      c.io.mosi.expect(true.B)
    }
  }

  it should "recieve 0b11001100" in {
    test(new Master()).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      c.clock.step(4)
      c.io.txData.poke("b01010101".asUInt())
      c.io.miso.poke(false.B)
      c.io.run.poke(true.B)
      c.clock.step(1)
      c.io.miso.poke(true.B)
      c.clock.step(1)
      c.io.run.poke(false.B)
      c.clock.step(1)
      c.io.miso.poke(true.B)
      c.clock.step(2)
      c.io.miso.poke(false.B)
      c.clock.step(2)
      c.io.miso.poke(false.B)
      c.clock.step(2)
      c.io.miso.poke(true.B)
      c.clock.step(2)
      c.io.miso.poke(true.B)
      c.clock.step(2)
      c.io.miso.poke(false.B)
      c.clock.step(2)
      c.io.miso.poke(false.B)
      c.clock.step(10)
      c.io.rxData.expect("b11001100".asUInt())
    }
  }
}
