#ifndef H_TRACE_UTILS
#define H_TRACE_UTILS


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
#include <string.h>
#include <elf.h>
#include <android/log.h>

int ptrace_setregs(pid_t pid, struct pt_regs * regs)
{
    if (ptrace(PTRACE_SETREGS, pid, NULL, regs) < 0) {
        printf("ptrace_setregs: Can not set register values\n");
        return -1;
    }

    return 0;
}

int ptrace_continue(pid_t pid)
{
    if (ptrace(PTRACE_CONT, pid, NULL, 0) < 0) {
        printf("ptrace_cont failed.\n");
        return -1;
    }

    return 0;
}


void getdata(pid_t pid,long addr,char* str,int len){
	char* laddr;
	int i,j;
	union u{
		long val;
		char chars[sizeof(long)];
	}data;
	
	i = 0;
	j = len / LONG_SIZE;
	laddr = str;
	while(i<j){
		data.val = ptrace(PTRACE_PEEKDATA,pid,addr + i * 4,NULL);
		memcpy(laddr,data.chars,LONG_SIZE);
		++i;
		laddr += LONG_SIZE;
	}

	j = len%LONG_SIZE;
	if(j != 0){
		data.val = ptrace(PTRACE_PEEKDATA,pid,addr+i*4,NULL);
		memcpy(laddr,data.chars,j);
	}
	
	str[len] = '\0';
}

void putdata(pid_t pid,long addr,char* str,int len){
	char* laddr;
	int i,j;
	union u{
		long val;
		char chars[LONG_SIZE];
	}data;

	i = 0;
	j = len / LONG_SIZE;
	laddr = str;
	while(i<j){
		memcpy(data.chars,laddr,LONG_SIZE);
		ptrace(PTRACE_POKEDATA,pid,addr+i*4,data.val);
		++i;
		laddr += LONG_SIZE;
	}

	j = len%LONG_SIZE;
	if(j != 0){
		memcpy(data.chars,laddr,j);
		ptrace(PTRACE_POKEDATA,pid,addr+i*4,data.val);
	}

}

void* get_module_base(pid_t pid,const char* module_name){
	FILE *fp;
	long addr = 0;
	char* pch;
	char filename[32];
	char line[1024];
	if(pid == 0){
		snprintf(filename,sizeof(filename),"/proc/self/maps");
	}else{
		snprintf(filename,sizeof(filename),"/proc/%d/maps",pid);
	}

	fp = fopen(filename,"r");

	if(fp != NULL){
		while(fgets(line,sizeof(line),fp)){
			if(strstr(line,module_name)){
				printf("yes,we got line: %s\n",line);
				pch = strtok(line,"_");
				addr = strtoul(pch,NULL,16);
				if(addr == 0x8000){
					printf("yes,addr == 0x8000\n");
					addr = 0;
				}
				break;
			}
		}
		fclose(fp);
	}

	return (void*)addr;
}

long get_remote_addr(pid_t target_pid,const char* module_name,void* local_addr){
	
	void* local_handle,*remote_handle;
	local_handle = get_module_base(0,module_name);
	remote_handle = get_module_base(target_pid,module_name);
	
	int i=0;
		
	printf("module_base: local[%p],remote[%p]\n",local_handle,remote_handle);
	
	long ret_addr = (long)((uint32_t)local_addr + (uint32_t)remote_handle - (uint32_t)local_handle);

	printf("remote_addr: [%p]\n",(void*)ret_addr);
	
	return ret_addr;
}

int ptrace_call(pid_t pid,long addr,long *params,uint32_t num_params,struct pt_regs* regs){
	printf("ptrace_call pid[%d] addr[%ld] ",pid,addr);	

	uint32_t i;
	for(i=0;i<num_params&&i<4;i++){
		regs->uregs[i] = params[i];
	}
	
	if(i<num_params){
		regs->ARM_sp -= (num_params-i)*LONG_SIZE;
		putdata(pid,(long)regs->ARM_sp,(char*)&params[i],(num_params-i)*LONG_SIZE);
	}
	
	regs->ARM_pc = addr;
	if(regs->ARM_pc&1){
		regs->ARM_pc &= (~1u);
		regs->ARM_cpsr |= CPSR_T_MASK;
	}else{
		regs->ARM_cpsr &= ~CPSR_T_MASK;
	}

	regs->ARM_lr = 0;

	printf("before setregs && conginue\n");

	if(ptrace_setregs(pid,regs) == -1 || ptrace_continue(pid) == -1){
		printf("error\n");
		return -1;
	}

	printf("after setregs && conginue\n");


	int stat = 0;
	waitpid(pid,&stat,WUNTRACED);
	printf("stat %x\n",stat);
	while(stat != 0xb7f){
		if(ptrace_continue(pid) == -1){
			printf("error\n");
			return -1;
		}
		waitpid(pid,&stat,WUNTRACED);
		printf("stat %x\n", stat);
	}//

	return 0;
}

void inject(pid_t pid,char* lib_path,void* func_ptr,long* params,size_t parms_len){

	struct pt_regs old_regs,regs;
	long func_addr;
	
	ptrace(PTRACE_GETREGS,pid,NULL,&old_regs);
	memcpy(&regs,&old_regs,sizeof(regs));

	printf("getting remote sleep_addr:\n");
	func_addr = get_remote_addr(pid,lib_path,func_ptr);

	ptrace_call(pid,func_addr,params,parms_len,&regs);
	
	ptrace_setregs(pid, &old_regs);
	//ptrace_continue(pid);
}

/**
void injectSo(pid_t pid,char* so_path,char* function_name,char* parameter){
	struct pt_regs old_regs,regs;
	long mmap_addr,dlopen_addr,dlsym_addr,dlclose_addr;

	ptrace(PTRACE_GETREGS,pid,NULL,&old_regs);
	memcpy(&regs,&old_regs,sizeof(regs));

	printf("getting remote address:\n");
	mmap_addr = get_remote_addr(pid,libc_path,(void*)mmap);
	dlopen_addr = get_remote_addr(pid,libc_path,(void*)dlopen);
	dlsym_addr = get_remote_addr(pid,libc_path,(void*)dlsym);
	dlclose_addr = get_remote_addr(pid,libc_path,(void*)dlclose);
	
	printf("mmap_addr=%p dlopen_addr=%p dlsym_addr=%p dlclose_addr=%p\n",
	(void*)mmap_addr,(void*)dlopen_addr,(void*)dlsym_addr,(void*)dlclose_addr);
	
	long parameters[10];

//mmap
	parameters[0] = 0;//address
	parameters[1] = 0x4000;//size
	parameters[2] = PROT_READ | PROT_WRITE | PROT_EXEC;//wrx
	parameters[3] = MAP_ANONYMOUS | MAP_PRIVATE;//flag
	parameters[4] = 0;//fd
	parameters[5] = 0;//offset

	ptrace_call(pid,mmap_addr,parameters,6,&regs);
	
	ptrace(PTRACE_GETREGS,pid,NULL,&regs);
	long map_base = regs.ARM_r0;
	printf("map_base=%p\n",(void*)map_base);

//dlopen
	printf("save so_path=%s to map_base = %p\n",so_path,(void*)map_base);
	putdata(pid,map_base,so_path,strlen(so_path)+1);

	parameters[0] = map_base;
	parameters[1] = RTLD_NOW | RTLD_GLOBAL;

	ptrace_call(pid,dlopen_addr,parameters,2,&regs);

	ptrace(PTRACE_GETREGS,pid,NULL,&regs);
	long function_ptr = regs.ARM_r0;

	printf("function_ptr=%p\n",(void*)function_ptr);

//function_call
	printf("save parameters=%s to map_base=%p\n",parameters,(void*)map_base);
	putdata(pid,map_base,parameters,strlen(parameters)+1);

	parameters[0] = map_base;

	ptrace_call(pid,funciont_ptr,parameters,1,&regs);

//dlclose
	parameters[0] = handle;
	ptrace_call(pid,dlclose_addr,parameters,1,&regs);

//restore old regs
	ptrace(PTRACE_SETREGS,pid,NULL,&old_regs);

}
**/

#endif