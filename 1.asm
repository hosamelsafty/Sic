Prbn01   START   1000
         LDA     =C'TEST'
         LDA     BETA
         MUL     GAMMA
         STA     ALPHA
         LTORG
GAMMA    WORD    4
ALPHA    EQU     1
BETA     EQU     ALPHA
DELTA    EQU    BETA-ALPHA
DATA     EQU     *
         LDA     =X'45'
         END     Prbn01
