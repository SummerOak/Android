#include <jni.h>
#include <string>

#include "define.h"
#include "TestClassA.h"
#include "TestClassB.h"
#include "ArtMethod.h"
#include "util/StringUtils.h"
#include "ArtModifiers.h"



extern "C" void art_method_handler(ArtMethod*);
extern "C" void call_old_art_method(int oriSP,int oriPC,void*);

ArtMethod* hookedMethod;
void* old_entry_point_from_quick_compiled_code_;

extern "C" void func1(int curSP,int curPC){

    LOGD("curSP = %x",curSP);
    LOGD("curPC = %x",curPC);

    LOGD("call_old_art_method %x",old_entry_point_from_quick_compiled_code_);
    call_old_art_method(curSP,curPC,old_entry_point_from_quick_compiled_code_);


}

extern "C" void func2(int r0,int r1,int r2,int r3,int r4,int r5,int r6){
}

extern "C" void chedifier_printf(int i){
    LOGD("chedifier_printf %d,%x",i,i);
}

void hookHandler(char* p){
    LOGD("hookHandler: ");

    hookedMethod->clearAccessFlag(kAccNative);

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

          LOGD("old_et: %x",old_entry_point_from_quick_compiled_code_);


            int r0 = 0,r1 = 0,r2 = 0,r3 = 0,r4 = 0,r5 = 0,r6 = 0;

            asm(
                    "mov %[r0],r0\n\r"
                    "mov %[r1],r1\n\r"
                    "mov %[r2],r2\n\r"
                    "mov %[r3],r3\n\r"
                    "mov %[r4],r4\n\r"
                    "mov %[r5],r5\n\r"
                    "mov %[r6],r6\n\r"

            : [r0]"=r"(r0),[r1]"=r"(r1),[r2]"=r"(r2), [r3]"=r"(r3), [r4]"=r"(r4), [r5]"=r"(r5), [r6]"=r"(r6)
            :
            );

            LOGD("r0=%d  r1=%d  r2=%d  r3=%d  r4=%d  r5=%d  r6=%d",r0,r1,r2,r3,r4,r5,r6);

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

    int r0 = 0,r1 = 0,r2 = 0,r3 = 0,r4 = 0,r5 = 0,r6 = 0;

    asm(
            "mov %[r0],x0\n\r"
            "mov %[r1],x1\n\r"
            "mov %[r2],x2\n\r"
            "mov %[r3],x3\n\r"
            "mov %[r4],x4\n\r"
            "mov %[r5],x5\n\r"
            "mov %[r6],x6\n\r"

    : [r0]"=r"(r0),[r1]"=r"(r1),[r2]"=r"(r2), [r3]"=r"(r3), [r4]"=r"(r4), [r5]"=r"(r5), [r6]"=r"(r6)
    :
    );

    LOGD("r0=%d  r1=%d  r2=%d  r3=%d  r4=%d  r5=%d  r6=%d",r0,r1,r2,r3,r4,r5,r6);

   /* asm(
    "mov w0, w0\n\r"
//    "ldr offset var1\n\r"
    "bl func1\n\r"

//    "str w0, offset var1\n\r"

    );*/





#else
    #define ABI "unknown"
#endif

    LOGD(ABI);

}


extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_hook_hook_Hook_hookMethodNative(JNIEnv *env,
                                                       jclass type,
                                                       jclass c,jstring name,jstring sig,jboolean isStatic,
                                                       jclass proxyC,jstring proxyName,jstring proxySig,jboolean isProxyStatic) {

    LOGD("hookMethodNative");

    const char* sName = StringUtils::ConvertJString2CStr(env,name);
    const char* sSig = StringUtils::ConvertJString2CStr(env,sig);
    const char* sProxyName = StringUtils::ConvertJString2CStr(env,proxyName);
    const char* sProxySig = StringUtils::ConvertJString2CStr(env,proxySig);

    LOGD("sName %s",sName);
    LOGD("sSig %s",sSig);
    LOGD("sProxyName %s",sProxyName);
    LOGD("sProxySig %s",sProxySig);

    jmethodID targetMethod;
    if(isStatic){
        targetMethod = env->GetStaticMethodID(c,sName,sSig);
    }else{
        targetMethod = env->GetMethodID(c,sName,sSig);
    }

    ArtMethod* targetArtMethod = reinterpret_cast<ArtMethod*>(targetMethod);
    targetArtMethod->print();

    LOGD(">>>>>>>>>>>>>>>>>>>>>>>");

    targetArtMethod->setAccessFlag(kAccNative);
    old_entry_point_from_quick_compiled_code_ = targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_;
    targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_ = nullptr;
//    targetArtMethod->ptr_sized_fields_.entry_point_from_interpreter_ = nullptr;
    targetArtMethod->SetEntryPointFromJni(reinterpret_cast<const void*>(art_method_handler));
    targetArtMethod->setCompiledCodeEntry(reinterpret_cast<const void*>(art_method_handler));

    targetArtMethod->print();
    hookedMethod = targetArtMethod;


    /*jmethodID hookMethod;
    if(isProxyStatic){
        hookMethod = env->GetStaticMethodID(proxyC,sProxyName,sProxySig);
    }else{
        hookMethod = env->GetMethodID(proxyC,sProxyName,sProxySig);
    }

    ArtMethod* hookArtMethod = reinterpret_cast<ArtMethod*>(hookMethod);
    hookArtMethod->print();*/

}

