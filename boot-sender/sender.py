import serial

import struct

use_port = '/dev/ttyUSB0'

ser = serial.Serial(use_port)
ser.baudrate = 115200
ser.parity = serial.PARITY_NONE
ser.bytesize = serial.EIGHTBITS
ser.stopbits = serial.STOPBITS_ONE
ser.timeout = 5 #sec            

ser.write(3)
ser.write(3)
ser.write(3)
