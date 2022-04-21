path = './hack/init.bin'

N = 2048
with open(path, mode='w') as f:
    for i in range(N):
        f.write("0000000000000000\n")
