import serial

import struct
from struct import pack
import time

use_port = '/dev/ttyUSB0'

_serial = serial.Serial(use_port)
_serial.baudrate = 115200
_serial.parity = serial.PARITY_NONE
_serial.stopbits = serial.STOPBITS_ONE
_serial.rtscts = True
# _serial.timeout = 3 #sec    z    

i = pack('B', 98)
_serial.write(i)
result = _serial.read(1)
print(result)

_serial.close()