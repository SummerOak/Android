//
// Created by Administrator on 2016/5/21.
//

#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/inotify.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <dlfcn.h>
#include <sys/stat.h>
#include <sys/ptrace.h>

#include "log.h"
#include "common.h"

/**
 *  Lock the file, this is block method.
 */
int lock_file(const char *lock_file_path) {
    int lockFileDescriptor = open(lock_file_path, O_RDONLY);
    if (lockFileDescriptor == -1) {
        lockFileDescriptor = open(lock_file_path, O_CREAT, S_IRUSR);
    }

    LOGD("try to flock start %s",lock_file_path);
    int lockRet = 0;//flock(lockFileDescriptor, LOCK_EX);
    LOGD("try to flock end %s",lock_file_path);

    return lockRet == -1 ? 0 : 1;
}

void createSelfAndDelOther(const char *self, const char *other) {
    int observer_self_descriptor = open(self, O_RDONLY);
    if (observer_self_descriptor == -1) {
        observer_self_descriptor = open(self, O_CREAT, S_IRUSR | S_IWUSR);
    }
    int observer_daemon_descriptor = open(other, O_RDONLY);
    while (observer_daemon_descriptor == -1) {
        usleep(1000);
        observer_daemon_descriptor = open(other, O_RDONLY);
    }


    remove(other);

    LOGE("Watched >>>>OBSERVER<<<< has been ready...");
}

/**
 *  get the android version code
 */
int getVersion(){
    char value[8] = "";
    DynamicallyLoadRealSystemPropertyGet()("ro.build.version.sdk", value);
    return atoi(value);
}

SystemPropertyGetFunction* DynamicallyLoadRealSystemPropertyGet() {
    // libc.so should already be open, get a handle to it.
    void* handle = dlopen("libc.so", 4);
    if (!handle) {
        LOGD("Cannot dlopen libc.so");
    }
    SystemPropertyGetFunction* real_system_property_get = reinterpret_cast<SystemPropertyGetFunction*>(
            dlsym(handle, "__system_property_get"));
    if (!real_system_property_get) {
        LOGD("Cannot resolve __system_property_get()");
    }
    return real_system_property_get;
}

char *strCombine(const char *str1, const char *str2, const char *str3){
    char *result;
    result = (char*) malloc(strlen(str1) + strlen(str2) + strlen(str3) + 1);
    if (!result){
        return NULL;
    }
    strcpy(result, str1);
    strcat(result, str2);
    strcat(result, str3);
    return result;
}

void startTargetService(const char* pkgName,const char* svcName){
    LOGD("startTargetService111");
    pid_t pid = fork();
    if(pid < 0){
        //error, do nothing...
    }else if(pid == 0){
        if(pkgName == NULL || svcName == NULL){
            exit(EXIT_SUCCESS);
        }
//        int version = getVersion();
        char* pkg$svc$name = strCombine(pkgName, "/", svcName);
//        if (version >= 17 || version == 0) {
            execlp("am", "am", "startservice", "--user", "0", "-n", pkg$svc$name, (char *) NULL);
//        } else {
//            execlp("am", "am", "startservice", "-n", pkg$svc$name, (char *) NULL);
//        }
        exit(EXIT_SUCCESS);
    }else{
        waitpid(pid, NULL, 0);
    }
}

void trace(pid_t pid){

    long traceRls = ptrace(PTRACE_ATTACH,pid,NULL,NULL);
    LOGD("%d attach %d result: %d",pid,getpid(),traceRls);

    if(traceRls == -1){
        LOGD("attach failed: %d",traceRls);
        return;
    }
    while(1){

        wait(NULL);

        LOGD("something happend");

        traceRls = ptrace(PTRACE_CONT, pid,NULL, NULL);
        LOGD("PTRACE_CONT result: %d",traceRls);
    }
}

void javaCallback(JNIEnv* env, jobject jobj, char* method_name){
    jclass cls = env->GetObjectClass(jobj);
    jmethodID cb_method = env->GetMethodID(cls, method_name, "()V");
    env->CallVoidMethod(jobj, cb_method);
}

void atexit_(void){
    LOGE("atexit_");
}

