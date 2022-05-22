import serial

import struct

use_port = '/dev/ttyUSB0'

_serial = serial.Serial(use_port)
_serial.baudrate = 9600
_serial.parity = serial.PARITY_NONE
_serial.bytesize = serial.EIGHTBITS
_serial.stopbits = serial.STOPBITS_ONE
_serial.timeout = 5 #sec            