import serial
from tqdm import tqdm

import struct
from struct import pack
import time



def send_insts(insts: list[(int, int)], num_inst: int):
    use_port = '/dev/ttyUSB0'

    _serial = serial.Serial(use_port)
    _serial.baudrate = 115200 
    _serial.parity = serial.PARITY_NONE
    _serial.stopbits = serial.STOPBITS_ONE
    _serial.rtscts = True
    # _serial.timeout = 3 #sec    z    
    i = pack('B', 98)
    t = 0


    lst0 = []
    lst1 = []
    for i in tqdm(range(num_inst)):
        fst = 0
        snd = 0
        if i < len(insts):
            x = insts[i]
            fst = x[0]
            snd = x[1]
        time.sleep(t)
        fst = pack('B', fst)
        _serial.write(fst)    
        y0 = _serial.read(1)
        lst0.append(y0)

        time.sleep(t)
        snd = pack('B', snd)
        _serial.write(snd)    
        y1 = _serial.read(1)
        lst1.append((fst == y0, snd == y1, ))

    _serial.close()


def load_inst_from_file(path: str) -> None:
    # auxiary function for converting string to (u8, u8)
    def aux(s: str):
        x = s.strip('\n')
        #        fst            snd
        #   |16-----------8----------------0|
        x = (int(x[0:8], 2), int(x[8:16], 2))
        return x
    

    with open(path) as f:
        content = list(map(aux, f.readlines()))
        return content

def main():
    content = load_inst_from_file("./bin.hack")
    send_insts(content, 10000)
    return 0

if __name__ == '__main__':
    main()
