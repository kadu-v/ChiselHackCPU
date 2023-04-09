// recieve 

// |-----|----------------|
// |  0  | address        |
// |  1  | instruction    |
// |  2  | uart value     |
// |  3  | loop counter0  |
// |  4  | loop counter1  |
// |  5  | RX status      |

// 25_000 命令を受け取る
    @2
    D=A
    @8201
    M=D      // RAM[8201] <- 3
    @25000
    D=A   
    @3
    M=D     // set a loop counter0 to RAM[3]
(L0)
// uart から値fstを受け取る 
    @UART0
    0;JMP
(RET.UART0)
// 受け取った値を８倍する
    @1
    M=0 // RAM[1] <- 0
    @2
    D=M // D <- RAM[2]
    @1
    M=D // M <- D = RAM[2]
    M=D+M     // 1
    D=M   
    M=D+M   // 2
    D=M
    M=D+M
    D=M
    M=D+M
    D=M
    M=D+M
    D=M
    M=D+M
    D=M
    M=D+M
    D=M
    M=D+M
// uart から値sndを受け取る 
    @UART1
    0;JMP
(RET.UART1)
    @2
    D=M     // set a uart value to the D register
    @1
    M=D+M   // add an instruction and a uart value
//    @SEND
//    0;JMP
//(RET.SEND)
// SRAMに命令を書き込む
    @SRAM
    0;JMP
(RET.SRAM)
    @0
    M=M+1    // RAM[0] <- RAM[0] + 1
    @3
    M=M-1
    D=M
    @L0
    D;JGT   // if loop counter0 > 0 then jump to L0
// DRAM -> SRAM
    @SWITCH
    0;JMP

// Uartから値を受け取りDRAM[2]メモリに書き込む
(UART0)
    @5
    M=1 // initialize status flag
(WHILE0)
// 条件チェック
    @1
    D=A
    @5
    D=D&M
    D=D-1
    @L3
    D;JEQ      // (status & 1) - 1 == 0
    @2
    D=A
    @5
    D=D&M
    D=D-1
    D=D-1
    @L3
    D;JNE       // ~((status & 2) - 2 == 0) ~> (status & 2) - 2 != 0
    @END.WHILE0
    0;JMP
(L3)
    @8192
    D=M
    @5
    M=D       // RAM[5] <- RAM[8192]
    @WHILE0
    0;JMP
(END.WHILE0)
    @8193
    D=M
    @2
    M=D      // RAM[2] <- RAM[8193]
    @16
    D=A
    @8192
    M=D      // clear RX buffer
    @RET.UART0
    0;JMP

(UART1)
    @5
    M=1 // initialize status flag
(WHILE1)
// 条件チェック
    @1
    D=A
    @5
    D=D&M
    D=D-1
    @L4
    D;JEQ      // (status & 1) - 1 == 0
    @2
    D=A
    @5
    D=D&M
    D=D-1
    D=D-1
    @L4
    D;JNE       // ~((status & 2) - 2 == 0) ~> (status & 2) - 2 != 0
    @END.WHILE1
    0;JMP
(L4)
    @8192
    D=M
    @5
    M=D       // RAM[5] <- RAM[8192]
    @WHILE1
    0;JMP
(END.WHILE1)
    @8193
    D=M
    @2
    M=D      // RAM[2] <- RAM[8193]
    @16
    D=A
    @8192
    M=D      // clear RX buffer
    @RET.UART1
    0;JMP

// メモリに書き込む
(SRAM)
    @0
    D=M
    @8199
    M=D    // RAM[8199] <- RAM[0]
    @1
    D=M
    @8200
    M=D    // RAM[8200] <- RAM[1]
    @32
    D=A
    @8198
    M=D    // RAM[8198] <- 32
    @RET.SRAM
    0;JMP

// インストラクションメモリをDRAM -> SRAMに変更
(SWITCH)
    @20000
    D=A
(LOOP)
    D=D-1
    @END.LOOP
    D;JEQ
    @LOOP
    0;JMP
(END.LOOP)
    @3
    D=A
    @8201
    M=D      // RAM[8201] <- 3
    @16
    D=A
    @8198
    M=D    // RAM[8198] <- 16
// 無限ループ
(END)
    @END
    0;JMP

(SEND)
    @0
    D=M
    @8194
    M=D
    @4096
    D=A
    @8192
    M=D
    @RET.SEND
    0;JMP