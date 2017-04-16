#pragma ide diagnostic ignored "CannotResolve"
#include <jni.h>

#include <stddef.h>
//#include <string.h>
#include "B.h"

#include "common.h"
#include "PureVirtualV.h"
#include "test/UcTransientMemoryPool.h"

#include <stdlib.h>
#include <unistd.h>

static B* pB = NULL;
static PureVirtualV* pV = NULL;

void testMemoryPool(){

//    UcTransientMemoryPool_c* pool = UcTransientMemoryPool_c::getInstance();
//    LOGD("alloc: %x",pool->trAlloc(10));



    return;
}

void testVirtualTable(){
    PureVirtualV* pV = new B;
    LOGD("before delete pV = %p",pV);
//    PureVirtualV* pV2 = pV;
    delete pV;
    LOGD("after delete pV = %p",pV);
    pV->test();

    return ;
}

void testThread(char* desc){
    if(gettid() == getpid()){
        LOGD("%s: running on ui thread: tid = %d ,pid = %d",desc,gettid(),getpid());
    }else{
        LOGD("%s: running on worker thread: tid = %d, pid = %d",desc,gettid(),getpid());
    }

    return;
}

extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_chedifier_test_NativeTest_test(JNIEnv *env,jclass type,jstring str){

    LOGD("native test2");

    const char *cStr = env->GetStringUTFChars(str,0);

    void* pp = malloc(0);

//    testThread((char*)cStr);

//    Monitor::test();

    pB = new B;

//    while(1){
//    int nContentLen = 10;
//    char* tmp = new char[nContentLen + 1];
//    memcpy(tmp, (void*)0x1, nContentLen);
//        void* p = malloc(1000);
//        memset(p,0,sizeof(p));
//    }

//    delete pB;
//    pB = NULL;
//    pB->call_memcopy((char*)0x6);


//    testMemoryPool();

    env->ReleaseStringUTFChars(str, cStr);

    return 0;
}


extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_chedifier_test_NativeTest_eatMemory(JNIEnv *env,jclass type,jint size){

    void* p = malloc(size);

    if(p != NULL){
        memset(p,0,size);
    }

    LOGD("malloc %d bytes return %p",size,p);

    return p==NULL?0:size;
}

namespace __cxxabiv1 {
    extern "C" void __cxa_pure_virtual() {
        while (1){
//            LOGD("call __cxa_pure_virtual");
        }
    }
}
