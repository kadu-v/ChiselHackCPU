package core

import chiseltest._
import org.scalatest._
import chisel3._

class BlinkSpec extends FlatSpec with ChiselScalatestTester with Matchers {
  behavior of "LED"
  it should "Blink" in {
    test(new Blink(4)) { c =>
      c.clock.step(16)
      c.io.out.expect(true.B)
      c.clock.step(16)
      c.io.out.expect(false.B)
    }
  }
}
