package top

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import chisel3._
import chiseltest.WriteVcdAnnotation
import ip.interface.uart.{Rx, Tx}

class CoreWithSpiSpec
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Matchers {

  behavior of "SPI Master"
}
