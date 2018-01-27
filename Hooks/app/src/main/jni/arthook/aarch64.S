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

.equ COPY_STACK_SIZE, 60
.equ TEMP_SPACE_SIZE, 32

.extern hook_func_proxy
ENTRY art_method_handler

    push {lr}
	push {r4,r5,r6,r7}
	mov r4,sp
	add r4,#20

	vpush {s0-s15}  @save s0-s15

    mov r5,#16
    push {r5}       @push fArgLen

    mov r5,sp
    add r5,#4
    push {r5}       @push fArgs

	push {r4}       @push oldSP

	push {r7}       @push r7
	push {r6}       @push r6

    mov r5,r4
    sub r5,#16
    ldr r5,[r5]
	push {r5}       @push r5

	sub r4,#8
	ldr r4,[r4]
	push {r4}       @push r4

    blx hook_func_proxy     @hook_func_proxy(r0,r1,r2,r3,r4,r5,r6,r7,oldSP,fArgs,fArgLen)

    pop {r4}
    pop {r5}
    pop {r6}
    pop {r7}
    pop {r5}
    pop {r5}
    pop {r5}
    vpop {s0-s15}

    pop {r4,r5,r6,r7}
    pop {pc}
	
END art_method_handler

/**

r0 = r0
r1 = r1
r2 = r2
r3 = r3
[sp] = r4
[sp+4] = r5
[sp+8] = r6
[sp+12] = r7
[sp+16] = oldSP
[sp+20] = fArgs
[sp+24] = entry

**/
ENTRY call_art_method

    push {lr}
    sub sp,#(COPY_STACK_SIZE+TEMP_SPACE_SIZE)
    str r0,[sp,#(COPY_STACK_SIZE+0)]
    str r1,[sp,#(COPY_STACK_SIZE+4)]
    str r2,[sp,#(COPY_STACK_SIZE+8)]
    str r3,[sp,#(COPY_STACK_SIZE+12)]
    str r4,[sp,#(COPY_STACK_SIZE+16)]
    str r5,[sp,#(COPY_STACK_SIZE+20)]
    str r6,[sp,#(COPY_STACK_SIZE+24)]
    str r7,[sp,#(COPY_STACK_SIZE+28)]
    mov r0,sp
    ldr r1,[sp,#(COPY_STACK_SIZE+TEMP_SPACE_SIZE+4+16)]        @ r1 = oldSP
    mov r2,#COPY_STACK_SIZE
    blx memcpy
    ldr r0,[sp,#(COPY_STACK_SIZE+0)]
    ldr r1,[sp,#(COPY_STACK_SIZE+4)]
    ldr r2,[sp,#(COPY_STACK_SIZE+8)]
    ldr r3,[sp,#(COPY_STACK_SIZE+12)]
    ldr r4,[sp,#(COPY_STACK_SIZE+16)]
    ldr r5,[sp,#(COPY_STACK_SIZE+20)]
    ldr r6,[sp,#(COPY_STACK_SIZE+24)]
    ldr r7,[sp,#(COPY_STACK_SIZE+28)]

    ldr r5,[sp,#(COPY_STACK_SIZE+TEMP_SPACE_SIZE+4+20)]
    mov ip,r5
    vldm   ip, {s0-s15}

    ldr r5,[sp,#(COPY_STACK_SIZE+TEMP_SPACE_SIZE+4+24)]
    blx r5
    add sp,#(COPY_STACK_SIZE+TEMP_SPACE_SIZE)
    pop {pc}
	
END call_art_method
