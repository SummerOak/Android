#ifndef H_SYS_CALL_FILTER
#define H_SYS_CALL_FILTER

#include <stdio.h>
#include <stdlib.h>
#include <asm/user.h>
#include <asm/ptrace.h>
#include <sys/ptrace.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <dlfcn.h>
#include <dirent.h>
#include <unistd.h>

#define SYS_CALL_NUM 1000
int sys_calls[SYS_CALL_NUM];

void init_sys_call_filter(){
	memset(sys_calls,0,sizeof(sys_calls));
}

long getSysCallNo(int pid,struct pt_regs *regs){
	
	long scno = 0;
	scno = ptrace(PTRACE_PEEKTEXT,pid,(void*)(regs->ARM_pc -4),NULL);
	if(scno == 0){
		return 0;
	}

	if(scno == 0xef000000){
		scno = regs->ARM_r7;
	}else{
		if((scno & 0x0ff00000) != 0x0f900000){
			return -1;
		}
	}
	return scno;
}

void onSysCall(pid_t pid,void(*before)(pid_t,int,struct pt_regs*),void(*after)(pid_t,int,struct pt_regs*)){
	
	struct pt_regs regs;
	int id = 0;

	ptrace(PTRACE_GETREGS,pid,NULL,&regs);
	id = getSysCallNo(pid,&regs);
	LOGD("onSysCall id = %d\n",id);

	if(0<id&&id<SYS_CALL_NUM){

		if(sys_calls[id] == 0){
			LOGD("sys call before %d\n",id);
			before(pid,id,&regs);
			sys_calls[id] = 1;
		}else if(sys_calls[id] == 1){
			LOGD("sys call after %d\n",id);
			after(pid,id,&regs);
			sys_calls[id] = 0;
		}
	}

}

#endif