Prbn06   START   1000
         LDCH    =C' '
LOOP     STCH    STR,X
         TIX     K20
While	 EQU	 LOOP+17
         JLT     LOOP
         ORG	 While+17
hossam   EQU     100+*
         LTORG
         LDCH    =X'01'
         LTORG
STR      BYTE    C'EOF'
.STR2     BYTE    C'0123456789666565656565'
. OUT OF RANGE
			LDX     =*
BLANK    BYTE    C' '
BUFFER   RESB    90050
K20      WORD    20
ZERO     WORD    0
         END     Prbn06