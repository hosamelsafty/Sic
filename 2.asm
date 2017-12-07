Prbn01   START   1000
STAB     RESB    1100
         ORG     STAB
ALPHA    RESB    6
BETA     RESB    3
GAMMA    RESB    1
         ORG     STAB+1100
         LDA     ALPHA
         END     Prbn01
