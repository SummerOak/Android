//
// Created by Administrator on 2016/10/10.
//

#include <jni.h>
#include <string.h>
#include "Utils.h"
#include "common.h"

void Utils::logoutABI(){

#if defined(__arm__)
    #if defined(__ARM_ARCH_7A__)
      #if defined(__ARM_NEON__)
        #if defined(__ARM_PCS_VFP)
          #define ABI "armeabi-v7a/NEON (hard-float)"
        #else
          #define ABI "armeabi-v7a/NEON"
        #endif
      #else
        #if defined(__ARM_PCS_VFP)
          #define ABI "armeabi-v7a (hard-float)"
        #else
          #define ABI "armeabi-v7a"
        #endif
      #endif
    #else
     #define ABI "armeabi"
    #endif
#elif defined(__i386__)
    #define ABI "x86"
#elif defined(__x86_64__)
    #define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
    #define ABI "mips64"
#elif defined(__mips__)
    #define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
    #define ABI "unknown"
#endif

    LOGD("abi = %s",ABI);

}

int Utils::getSystemAPLevel(JNIEnv *env){
    // VERSION is a nested class within android.os.Build (hence "$" rather than "/")
    jclass versionClass = env->FindClass("android/os/Build$VERSION");
    if (NULL == versionClass){
        return -1;
    }

    jfieldID sdkIntFieldID = env->GetStaticFieldID(versionClass, "SDK_INT", "I");
    jint sdkInt = env->GetStaticIntField(versionClass, sdkIntFieldID);
    return sdkInt;
}

float Utils::unsigned_int2_float(unsigned int x)
{
    int i;
    int iHighestBitLocation = 0;
    int iShiftBitNum = 0;
    int iExponentBias = 0;
    unsigned int iMemory = 0;
    float fResult = 0;
    if(0 == x)
    {
        return fResult;
    }
    //Find the location of highest bit
    for (  i = 31; i >= 0; i--)
    {
        if (1 == ((x >> i) & 0x1))
        {
            iHighestBitLocation = i + 1;
            break;
        }
    }
    // how many bits should be shift
    iShiftBitNum = iHighestBitLocation - 1;
    iExponentBias = iShiftBitNum + 127;
    for (i = 0; i < 8; i++)
    {
        if (1 == ((iExponentBias >> i) & 0x1))
        {
            iMemory = iMemory + (0x1 << (23 + i));
        }
    }
    //get rid of the "1.00000..."
    x = x - (0x1 << iShiftBitNum);
    for (i = 0; i < iShiftBitNum; i++)
    {
        if (1 == ((x >> i) & 0x1))
        {
            iMemory = iMemory + (0x1 << (23 - iShiftBitNum + i));
        }
    }
    memcpy(&fResult, &iMemory, sizeof(float));
    return fResult;
}