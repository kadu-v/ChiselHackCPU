	@256
	D=A
	@SP
	M=D
	@RETURN0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Sys.init
	0;JMP
(RETURN0)
(Memory.init)
	@SP
	A=M
	D=A
	@SP
	M=D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@Memory.0
	M=D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Memory.peek)
	@SP
	A=M
	D=A
	@SP
	M=D
	@0
	D=A
	@ARG
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@Memory.0
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@SP
	AM=M-1
	D=M
	@R4
	M=D
	@0
	D=A
	@THAT
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Memory.poke)
	@SP
	A=M
	D=A
	@SP
	M=D
	@0
	D=A
	@ARG
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@Memory.0
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@1
	D=A
	@ARG
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@SP
	AM=M-1
	D=M
	@R4
	M=D
	@R5
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@THAT
	M=D+M
	@SP
	AM=M-1
	D=M
	@THAT
	A=M
	M=D
	@0
	D=A
	@THAT
	M=M-D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Sys.init)
	@SP
	A=M
	D=A
	@SP
	M=D
	@20000
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@15000
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@SP
	AM=M-1
	D=M
	@Sys.0
	M=D
	@RETURN1
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Memory.init
	0;JMP
(RETURN1)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@RETURN2
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Uart.init
	0;JMP
(RETURN2)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@RETURN3
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Main.main
	0;JMP
(RETURN3)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@RETURN4
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Sys.halt
	0;JMP
(RETURN4)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Sys.halt)
	@SP
	A=M
	D=A
	@SP
	M=D
(Sys.halt$WHILE_EXP0)
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	A=M-1
	M=!M
	@SP
	A=M-1
	M=!M
	@SP
	AM=M-1
	D=M
	@Sys.halt$WHILE_END0
	D;JNE
	@Sys.halt$WHILE_EXP0
	0;JMP
(Sys.halt$WHILE_END0)
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Sys.error)
	@SP
	A=M
	D=A
	@SP
	M=D
	@RETURN5
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@5
	D=D-A
	@ARG
	M=D
	@Sys.halt
	0;JMP
(RETURN5)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(Main.main)
	@SP
	A=M
	M=0
	A=A+1
	M=0
	A=A+1
	M=0
	A=A+1
	M=0
	A=A+1
	D=A
	@SP
	M=D
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@2
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@2
	D=A
	@LCL
	M=M-D
	@100
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@3
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@3
	D=A
	@LCL
	M=M-D
(Main.main$WHILE_EXP0)
	@2
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@3
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	D=M-D
	@THEN0
	D;JLT
	@SP
	A=M-1
	M=0
	@ENDIF0
	0;JMP
(THEN0)
	@SP
	A=M-1
	M=-1
(ENDIF0)
	@SP
	A=M-1
	M=!M
	@SP
	AM=M-1
	D=M
	@Main.main$WHILE_END0
	D;JNE
	@16
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@8
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@1
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@1
	D=A
	@LCL
	M=M-D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@1
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@0
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@0
	D=A
	@LCL
	M=M-D
	@8199
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@2
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@RETURN6
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@7
	D=D-A
	@ARG
	M=D
	@Memory.poke
	0;JMP
(RETURN6)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@8200
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@0
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@RETURN7
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@7
	D=D-A
	@ARG
	M=D
	@Memory.poke
	0;JMP
(RETURN7)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@8198
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@32
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@RETURN8
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@7
	D=D-A
	@ARG
	M=D
	@Memoru.poke
	0;JMP
(RETURN8)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
	@2
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@1
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	M=D+M
	@2
	D=A
	@LCL
	M=D+M
	@SP
	AM=M-1
	D=M
	@LCL
	A=M
	M=D
	@2
	D=A
	@LCL
	M=M-D
	@Main.main$WHILE_EXP0
	0;JMP
(Main.main$WHILE_END0)
	@2
	D=A
	@LCL
	A=D+M
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@100
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	AM=M-1
	D=M
	@SP
	A=M-1
	D=M-D
	@THEN1
	D;JEQ
	@SP
	A=M-1
	M=0
	@ENDIF1
	0;JMP
(THEN1)
	@SP
	A=M-1
	M=-1
(ENDIF1)
	@SP
	AM=M-1
	D=M
	@Main.main$IF_TRUE0
	D;JNE
	@Main.main$IF_FALSE0
	0;JMP
(Main.main$IF_TRUE0)
	@8201
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@1
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@RETURN9
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@LCL
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@ARG
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THIS
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@THAT
	D=M
	@SP
	AM=M+1
	A=A-1
	M=D
	@SP
	D=M
	@LCL
	M=D
	@7
	D=D-A
	@ARG
	M=D
	@Memory.poke
	0;JMP
(RETURN9)
	@SP
	AM=M-1
	D=M
	@R5
	M=D
(Main.main$IF_FALSE0)
	@0
	D=A
	@SP
	AM=M+1
	A=A-1
	M=D
	@5
	D=A
	@LCL
	A=M-D
	D=M
	@R13
	M=D
	@SP
	A=M-1
	D=M
	@ARG
	A=M
	M=D
	D=A+1
	@SP
	M=D
	@LCL
	AM=M-1
	D=M
	@THAT
	M=D
	@LCL
	AM=M-1
	D=M
	@THIS
	M=D
	@LCL
	AM=M-1
	D=M
	@ARG
	M=D
	@LCL
	AM=M-1
	D=M
	@LCL
	M=D
	@R13
	A=M
	0;JMP
(END)
	@END
	0;JMP
