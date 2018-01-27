//
// Created by wxj_pc on 2016/10/2.
//

#include <jni.h>
#include <stdio.h>
#include <common.h>

#include <sys_call_filter.h>
#include <trace_def.h>
#include <trace_utils.h>

int g_tracing = 0;
void injectSleep(pid_t pid);

void reverse(char* str){
	LOGD("revers %s",str);
	char* h = str;
	char* t = str + strlen(str) - 1;
	char temp;
	while( h < t){
		temp = (*h);
		(*h) = (*t);
		(*t) = temp;

		h++,t--;
	}
}

void modifyString(pid_t pid,long addr,long strlen){
	char* str;
	str = (char*)calloc((strlen+1)*sizeof(char),1);
	getdata(pid,addr,str,strlen);
	reverse(str);
	//LOGD("after reverse: %s",str);
	putdata(pid,addr,str,strlen);
}



void hookSysCallBefore(pid_t pid,int id,struct pt_regs* regs){
	if(id == __NR_write && regs != NULL){
		modifyString(pid,regs->ARM_r1,regs->ARM_r2);
	}
}

void hookSysCallAfter(pid_t pid,int id,struct pt_regs* regs){
	if(id == __NR_write){
		//injectSleep(pid);
	}
}

void injectSleep(pid_t pid){

	LOGD("inject start>>>>>>>>>>>>>>>>>>>\n");

	long sleep_params[2];
	sleep_params[0] = 2;
	sleep_params[1] = 2;
	inject(pid,"/system/lib/libc.so",(void*)sleep,sleep_params,2);

	LOGD("inject end>>>>>>>>>>>>>>>>>>>\n");
}

JNIEXPORT jint JNICALL
Java_example_chedifier_hook_ptrace_PTrace_pTraceNative(JNIEnv *env,
                                                       jclass type,
                                                       jint nPid) {

    pid_t pid = nPid;
    LOGD("trace [%d] >>>>>>>>>>>>>>>>>>>>>>>\n",pid);

    init_sys_call_filter();

    int status;
    int code = 0;
    if((code = ptrace(PTRACE_ATTACH,pid,NULL,NULL)) != 0){
        LOGD("trace process failed: %d\n",code);
        return 1;
    }

    /**
    	while(1){
    		injectSleep(pid);
    		sleep(7);
    	}
    **/

    g_tracing = 1;
    while(g_tracing){

        wait(&status);
        //LOGD("status[%x]\n",status);
        onSysCall(pid,hookSysCallBefore,hookSysCallAfter);
        ptrace(PTRACE_SYSCALL,pid,NULL,NULL);

    }

    if((code = ptrace(PTRACE_DETACH,pid,NULL,NULL)) != 0){
        LOGD("detach process failed: %d\n",code);
        return 1;
    }

    return 1;
}


JNIEXPORT jint JNICALL
Java_example_chedifier_hook_ptrace_PTrace_pTraceStopNative(JNIEnv *env,
                                                       jclass type,
                                                       jint nPid) {

    pid_t pid = nPid;
    LOGD("stop trace [%d] >>>>>>>>>>>>>>>>>>>>>>>\n",pid);

    g_tracing = 0;
}

