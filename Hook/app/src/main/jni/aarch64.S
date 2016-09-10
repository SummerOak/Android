.macro ENTRY name
    .thumb_func
    .type \name, #function
    .global \name
    /* Cache alignment for function entry */
    .balign 16
\name:
    .cfi_startproc
    .fnstart
.endm

.macro END name
    .fnend
    .cfi_endproc
    .size \name, .-\name
.endm

.extern chedifier_printf
.extern func1
.extern func2
ENTRY art_method_handler

	push {r0,r1,r2,r3}

    blx     func1   
	
END art_method_handler

ENTRY call_old_art_method
	
	blx		r2
	
END call_old_art_method
