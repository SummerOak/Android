#include <jni.h>
#include <string>

#include "define.h"
#include "ArtMethod.h"
#include "util/StringUtils.h"
#include "util/Utils.h"
#include "ArtModifiers.h"

extern "C" void art_method_handler(ArtMethod*);
extern "C" void call_old_art_method(unsigned int r0,
                                    unsigned int r1,
                                    unsigned int r2,
                                    unsigned int r3,
                                    unsigned int r4,
                                    unsigned int r5,
                                    unsigned int r6,
                                    unsigned int r7,
                                    unsigned int oldSP,
                                    unsigned int old_entry_point_from_quick_compiled_code_);


ArtMethod* hookedMethod;
unsigned int old_entry_point_from_quick_compiled_code_;

extern "C" void hook_func_proxy(
unsigned int r0,//
unsigned int r1,
unsigned int r2,
unsigned int r3,
unsigned int r4,
unsigned int r5,
unsigned int r6,
unsigned int r7,
unsigned int oldSP){

    LOGD("r0 = %x",r0);
    LOGD("r1 = %x",r1);
    LOGD("r2 = %x",r2);
    LOGD("r3 = %x",r3);
    LOGD("r4 = %x",r4);
    LOGD("r5 = %x",r5);
    LOGD("r6 = %x",r6);
    LOGD("r7 = %x",r7);
    LOGD("oldSP = %x",oldSP);
    LOGD("old_entry_point_from_quick_compiled_code_ %x",old_entry_point_from_quick_compiled_code_);

    call_old_art_method(
    r0,r1,r2,r3,r4,r5,r6,r7,
    oldSP,
    old_entry_point_from_quick_compiled_code_);
}

extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_hook_hook_Hook_hookMethodNative(JNIEnv *env,
                                                       jclass type,
                                                       jclass c,jstring name,jstring sig,jboolean isStatic,
                                                       jclass proxyC,jstring proxyName,jstring proxySig,jboolean isProxyStatic) {

    Utils::logoutABI();
    LOGD("hookMethodNative >>>");

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

    LOGD("replace code entry >>>");

//    targetArtMethod->setAccessFlag(kAccNative);
    old_entry_point_from_quick_compiled_code_ = reinterpret_cast<unsigned int>(targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_);
    targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_ = reinterpret_cast<void*>(art_method_handler);
//    targetArtMethod->ptr_sized_fields_.entry_point_from_interpreter_ = nullptr;
//    targetArtMethod->SetEntryPointFromJni(reinterpret_cast<const void*>(art_method_handler));
//    targetArtMethod->setCompiledCodeEntry(reinterpret_cast<const void*>(art_method_handler));

    LOGD("old_entry_point_from_quick_compiled_code_ [%p]\n",old_entry_point_from_quick_compiled_code_);
    LOGD("art_method_handler [%p]\n",art_method_handler);

    targetArtMethod->print();
    hookedMethod = targetArtMethod;
}

