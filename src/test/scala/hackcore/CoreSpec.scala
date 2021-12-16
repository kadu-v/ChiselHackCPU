package hackcore

import chiseltest._
import org.scalatest._
import chisel3._
import top.Top

class HackCoreSpec extends FlatSpec with ChiselScalatestTester with Matchers {
  behavior of "HackCPU"
  it should "test add.hex" in {
    test(new Top("./hack/Add.hack")) { c =>
      c.clock.step(100)
    }
  }
}
