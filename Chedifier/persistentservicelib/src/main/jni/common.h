//
// Created by Administrator on 2016/5/21.
//

#ifndef KEEPALIVESERVICE_COMMON_H
#define KEEPALIVESERVICE_COMMON_H

#include <jni.h>

#ifdef  __cplusplus
extern "C"{
#endif

typedef int (SystemPropertyGetFunction)(const char*, char*);

SystemPropertyGetFunction* DynamicallyLoadRealSystemPropertyGet();

char *strCombine(const char *str1, const char *str2, const char *str3);

int lock_file(const char* lock_file_path);

void createSelfAndDelOther(const char* self, const char *other);

int getVersion();

void trace(pid_t pid);

void startTargetService(const char* pkgName,const char* svcName);

void javaCallback(JNIEnv* env, jobject jobj, char* method_name);

void atexit_(void);

#ifdef  __cplusplus
}
#endif

#endif //KEEPALIVESERVICE_COMMON_H

