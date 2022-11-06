import serial

import struct

use_port = '/dev/ttyUSB0'

_serial = serial.Serial(use_port)
_serial.baudrate = 115200
_serial.parity = serial.PARITY_NONE
_serial.bytesize = serial.EIGHTBITS
_serial.stopbits = serial.STOPBITS_ONE
_serial.rtscts = True
_serial.timeout = 5 #sec        

for i in range(10):
    _serial.write(96)


result = _serial.read_all()
print(result)

_serial.close()