import serial
from tqdm import tqdm

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
cnt = 100
i = pack('B', 98)

lst = []
for i in tqdm(range(cnt)):
    time.sleep(0.05)
    x = pack('B', 98)
    _serial.write(x)    
    result = _serial.read(1)
    lst.append(result)

    time.sleep(0.1)
    x = pack('B', 99)
    _serial.write(x)    
    result = _serial.read(1)
    lst.append(result)
    print(lst)

    print(len(lst))
# time.sleep(1)
# result = _serial.read(1)
# print(result)
_serial.close()

for e in lst:
    print(e)