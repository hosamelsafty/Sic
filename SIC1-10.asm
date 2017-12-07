Prbn10   START   1000  
         LDX     ZERO
LOOP     TD      DEV05
         JEQ     LOOP
         LDCH    RECORD,X
         WD      DEV05
         TIX     K100
         JLT     LOOP 		 
RECORD   BYTE    c'1:0123456789 '
RECORD1  BYTE    c'2:0123456789 '
RECORD2  BYTE    c'3:0123456789 '
RECORD3  BYTE    c'4:0123456789 '
RECORD4  BYTE    c'5:0123456789 '
RECORD5  BYTE    c'6:0123456789 '
RECORD6  BYTE    c'7:0123456789 '
RECORD7  BYTE    c'8:0123456'
K100     WORD    100
ZERO     WORD    0
DEV05    BYTE    X'05'
         END     Prbn10
