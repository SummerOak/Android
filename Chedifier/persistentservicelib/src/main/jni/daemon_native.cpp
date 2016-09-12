//
// Created by Administrator on 2016/5/21.
//

#include <jni.h>
#include <stddef.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ptrace.h>

#include "log.h"
#include "common.h"

void startDaemonProcByExecl(const char *daemonProc,
                                const char *pkgName,
                                const char *svcName,
                                const char *selfL, const char *otherL,
                                const char *selfT, const char *otherT){
    LOGD("starting daemon proc... %s",daemonProc);

    int ret = execlp(daemonProc,daemonProc,
                     pkgName,svcName,
                     otherL,selfL,
                     otherT,selfT,(char*)NULL);

    LOGE("daemon proc execl failed: %d",ret);
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_uc_persistentservicelib_daemon_DeadFileLockMonitor_startNative(JNIEnv *env, jobject thiz,
                                                                    jstring hostLockFilePath,jstring daemonLockFilePath,
                                                                    jstring hostTempFilePath,jstring daemonTempFilePath) {

    LOGD("DeadFileLockMonitor_startNative");

    const char* selfL = env->GetStringUTFChars(hostLockFilePath,(jboolean *)0);
    const char* otherL = env->GetStringUTFChars(daemonLockFilePath,(jboolean *)0);
    const char* selfT = env->GetStringUTFChars(hostTempFilePath,(jboolean *)0);
    const char* otherT = env->GetStringUTFChars(daemonTempFilePath,(jboolean *)0);

    int lock_status = 0;
    int try_time = 0;
    while(try_time < 3 && !(lock_status = lock_file(selfL))){
        try_time++;
        LOGD("host lock file failed and try again as %d times", try_time);
        usleep(10000);
    }
    if(!lock_status){
        LOGE("host lock myself failed and exit");
        return ;
    }

    createSelfAndDelOther(selfT, otherT);

    LOGE("host try lock other");
    lock_status = lock_file(otherL);
    LOGE("host lock_file return: %d",lock_status);

    if(lock_status){
        LOGE("Watch >>>>DAEMON<<<<< Daed !!");
        remove(selfT);// it`s important ! to prevent from deadlock

        int t = 50;
        while(t-- > 0){
            javaCallback(env,thiz,"onDeathNative");
        }
    }

}

#ifdef __cplusplus
}
#endif



#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_uc_persistentservicelib_daemon_DeadFileLockMonitor2_startNative(JNIEnv *env, jobject thiz,
                                                                                               jstring packageName,
                                                                                               jstring serviceName,
                                                                                               jstring daemonProcPath,
                                                                                               jstring hostLockFilePath,jstring daemonLockFilePath,

                                                                                               jstring hostTempFilePath,jstring daemonTempFilePath) {

    LOGD("DeadFileLockMonitor2_startNative");

    atexit(atexit_);

    const char* pkgName = env->GetStringUTFChars(packageName,(jboolean *)0);
    const char* svcName = env->GetStringUTFChars(serviceName,(jboolean *)0);
    const char* daemonProc = env->GetStringUTFChars(daemonProcPath,(jboolean *)0);
    const char* selfL = env->GetStringUTFChars(hostLockFilePath,(jboolean *)0);
    const char* otherL = env->GetStringUTFChars(daemonLockFilePath,(jboolean *)0);
    const char* selfT = env->GetStringUTFChars(hostTempFilePath,(jboolean *)0);
    const char* otherT = env->GetStringUTFChars(daemonTempFilePath,(jboolean *)0);

    LOGD("packageName: %s svcName: %s daemonProc: %s",pkgName,svcName,daemonProc);

    pid_t pid = fork();
    if(pid == 0){

        pid_t gpid = fork();
        if(gpid == 0){
            setuid(1000);
            seteuid(1000);
            setgid(1000);
            setegid(1000);

            startDaemonProcByExecl(daemonProc, pkgName, svcName, selfL, otherL, selfT, otherT);

            LOGD(">>>>>>>>>>>>>>parent dead");
        }else{
            kill(getpid(),SIGKILL);
        }

//
    }else{

        waitpid(pid,NULL,0);

        LOGD(">>>>>>>>>>>>>>>child dead");

//        trace(pid);

        int lock_status = 0;
        int try_time = 0;
        while(try_time < 3 && !(lock_status = lock_file(selfL))){
            try_time++;
            LOGD("host lock file failed and try again as %d times", try_time);
            usleep(10000);
        }
        if(!lock_status){
            LOGE("host lock myself failed and exit");
            return ;
        }

        createSelfAndDelOther(selfT, otherT);

        LOGD("host try lock other");
        lock_status = lock_file(otherL);
        LOGD("host lock_file return: %d",lock_status);

        if(lock_status){
            LOGE("Watch >>>>DAEMON<<<<< Daed !!");
            remove(selfT);// it`s important ! to prevent from deadlock

            pid_t pid = fork();
            if(pid == 0) {
                startDaemonProcByExecl(daemonProc, pkgName, svcName, selfL, otherL, selfT,
                                           otherT);
            }
        }
    }

}

#ifdef __cplusplus
}
#endif



