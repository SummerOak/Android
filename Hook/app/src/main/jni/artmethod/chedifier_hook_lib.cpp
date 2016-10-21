#include <jni.h>
#include <string>
#include "../common/common.h"
#include "define.h"

#include "ArtMethod.h"
#include "../util/StringUtils.h"
#include "../util/Utils.h"
#include "ArtModifiers.h"
#include <map>

using namespace std;

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
                                    unsigned int fArgs,
                                    unsigned int old_entry_point_from_quick_compiled_code);

struct HookInfo{
    ArtMethod*  target_method;
    unsigned int old_entry_point_from_quick_compiled_code;
    ArtMethod* proxy_method;
    unsigned int proxy_entry_point_from_quick_compiled_code;
    const char* s_target_func_name;
    const char* s_target_func_signature;
    const char* s_proxy_func_name;
    const char* s_proxy_func_signature;
};

map<ArtMethod*,HookInfo*> hook_func_mapping;

HookInfo* findHookInfo(ArtMethod* target){

    for(map<ArtMethod*,HookInfo*>::iterator itr = hook_func_mapping.begin();itr!=hook_func_mapping.end();itr++){
        if(itr->first == target){
            return itr->second;
        }
    }

    return NULL;
}

extern "C" void hook_func_proxy(
unsigned int r0,        //method
unsigned int r1,        //?
unsigned int r2,        //?
unsigned int r3,        //?
unsigned int r4,
unsigned int r5,
unsigned int r6,
unsigned int r7,
unsigned int oldSP,
unsigned int fArgs,
unsigned int fArgLen
){

    LOGD("r0 = %x",r0);
    LOGD("r1 = %x",r1);
    LOGD("r2 = %x",r2);
    LOGD("r3 = %x",r3);
    LOGD("r4 = %x",r4);
    LOGD("r5 = %x",r5);
    LOGD("r6 = %x",r6);
    LOGD("r7 = %x",r7);
    LOGD("oldSP = %x",oldSP);
    LOGD("fArgs = %x",fArgs);
    LOGD("fArgLen = %x",fArgLen);

    ArtMethod* targetMethod = reinterpret_cast<ArtMethod*>(r0);
    HookInfo* pInfo = findHookInfo(targetMethod);
    if(pInfo == NULL){
        LOGD("damn,hook info lost.");
        return;
    }

    unsigned int* tArgs = reinterpret_cast<unsigned int*>(fArgs);
    for(int i=0;i<fArgLen;i++){
        LOGD("fArgs[%d] = %f",i,Utils::unsigned_int2_float(tArgs[i]));
    }

    LOGD("old_entry_point_from_quick_compiled_code %x",pInfo->old_entry_point_from_quick_compiled_code);
    LOGD("proxy_entry_point_from_quick_compiled_code %x",pInfo->proxy_entry_point_from_quick_compiled_code);

    call_old_art_method(
        reinterpret_cast<unsigned int>(pInfo->proxy_method),
        r1,r2,r3,r4,r5,r6,r7,
        oldSP,fArgs,
        pInfo->proxy_entry_point_from_quick_compiled_code);

    call_old_art_method(
        r0,r1,r2,r3,r4,r5,r6,r7,
        oldSP,fArgs,
        pInfo->old_entry_point_from_quick_compiled_code);
}

extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_hook_hook_Hook_hookMethodNative(JNIEnv *env,
                                                       jclass type,
                                                       jclass c,jstring name,jstring sig,jboolean isStatic,
                                                       jclass proxyC,jstring proxyName,jstring proxySig,jboolean isProxyStatic) {

    Utils::logoutABI();
    LOGD("hookMethodNative >>>");

    const char* target_method_name = StringUtils::ConvertJString2CStr(env,name);
    const char* target_method_signature = StringUtils::ConvertJString2CStr(env,sig);
    const char* proxy_method_name = StringUtils::ConvertJString2CStr(env,proxyName);
    const char* proxy_method_signature = StringUtils::ConvertJString2CStr(env,proxySig);

    LOGD("target_method_name: %s  target_method_signature: %s",target_method_name,target_method_signature);
    LOGD("proxy_method_name %s  proxy_method_signature %s",proxy_method_name,proxy_method_signature);

    jmethodID targetMethod;
    if(isStatic){
        targetMethod = env->GetStaticMethodID(c,target_method_name,target_method_signature);
    }else{
        targetMethod = env->GetMethodID(c,target_method_name,target_method_signature);
    }

    if(targetMethod == NULL){
        LOGD("targetMethod not found.");
        return 1;
    }

    jmethodID proxyMethod;
    if(isProxyStatic){
        proxyMethod = env->GetStaticMethodID(proxyC,proxy_method_name,proxy_method_signature);
    }else{
        proxyMethod = env->GetMethodID(proxyC,proxy_method_name,proxy_method_signature);
    }
    if(proxyMethod == NULL){
        LOGD("proxyMethod not found.");
        return 1;
    }

    ArtMethod* targetArtMethod = reinterpret_cast<ArtMethod*>(targetMethod);
    targetArtMethod->print();

    ArtMethod* proxyArtMethod = reinterpret_cast<ArtMethod*>(proxyMethod);
    proxyArtMethod->print();

    LOGD("replace code entry >>>");

//    targetArtMethod->setAccessFlag(kAccNative);
    unsigned int old_entry_point_from_quick_compiled_code = reinterpret_cast<unsigned int>(targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_);
    targetArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_ = reinterpret_cast<void*>(art_method_handler);
//    targetArtMethod->ptr_sized_fields_.entry_point_from_interpreter_ = nullptr;
//    targetArtMethod->SetEntryPointFromJni(reinterpret_cast<const void*>(art_method_handler));
//    targetArtMethod->setCompiledCodeEntry(reinterpret_cast<const void*>(art_method_handler));

    LOGD("old_entry_point_from_quick_compiled_code [%x]\n",old_entry_point_from_quick_compiled_code);
    LOGD("art_method_handler [%p]\n",art_method_handler);

    struct HookInfo *pInfo = new HookInfo();
    pInfo->target_method = targetArtMethod;
    pInfo->old_entry_point_from_quick_compiled_code = old_entry_point_from_quick_compiled_code;
    pInfo->proxy_method = proxyArtMethod;
    pInfo->proxy_entry_point_from_quick_compiled_code = reinterpret_cast<unsigned int>(proxyArtMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_);
    pInfo->s_target_func_name = target_method_name;
    pInfo->s_target_func_signature = target_method_signature;
    pInfo->s_proxy_func_name = proxy_method_name;
    pInfo->s_proxy_func_signature = proxy_method_signature;
    hook_func_mapping.insert(pair<ArtMethod*,HookInfo*>(targetArtMethod,pInfo));

    return 0;
}

