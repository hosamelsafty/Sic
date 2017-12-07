Prbn12   START   1000  
         LDX     ZERO
LOOP     TD      DEVF3
         JEQ     LOOP
         LDA     ZERO
         RD      DEVF3
         COMP    FOUR
         JEQ     DONE
         STCH    RECORD,X
         TIX     K100
         JLT     LOOP
DONE     STX     LENGTH 		 
LENGTH   RESW    1
RECORD   RESB    100
K100     WORD    100
ZERO     WORD    0                  END OF LINE
FOUR     WORD    4                  END OF FILE
DEVF3    BYTE    X'F3'
         END     Prbn12
