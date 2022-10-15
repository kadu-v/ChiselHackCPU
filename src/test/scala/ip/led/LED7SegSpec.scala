package ip.led

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation

class LED7SegSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  val freq = 1
  val ledRegAddr = 8200
  val waitTime = freq * 1000000 / 240
  behavior of "LED 7 Segments"
  it should "decode 0x44" in {

    test(new LED7Seg(freq, ledRegAddr))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(4)
        c.io.addrM.poke(ledRegAddr)
        c.io.inM.poke(0x44)
        c.io.writeM.poke(true.B)
        c.clock.step(1)
        c.io.writeM.poke(false.B)

        c.clock.step(1000)
        c.io.cs.expect(false.B)
        c.io.out.expect(0x33.asUInt)

        c.clock.step(5000)
        c.io.cs.expect(true.B)
        c.io.out.expect(0x33.asUInt)
      }
  }

  it should "decode 0x4a" in {

    test(new LED7Seg(freq, ledRegAddr))
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.clock.setTimeout(0)
        c.clock.step(4)
        c.io.addrM.poke(ledRegAddr)
        c.io.inM.poke(0x4a)
        c.io.writeM.poke(true.B)
        c.clock.step(1)
        c.io.writeM.poke(false.B)

        c.clock.step(1000)
        c.io.cs.expect(false.B)
        c.io.out.expect(0x77.asUInt)

        c.clock.step(5000)
        c.io.cs.expect(true.B)
        c.io.out.expect(0x33.asUInt)
      }
  }
}
