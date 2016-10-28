#include <jni.h>
#include <string>
#include "common.h"
#include "define.h"

#include "hook_type.h"
#include "IArtMethodAdapter.h"
#include "ArtMethodHelper.h"
#include "StringUtils.h"
#include "Utils.h"
#include "ArtModifiers.h"
#include <map>

using namespace std;

extern "C" void art_method_handler(uint32_t);
extern "C" void call_art_method(unsigned int r0,
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
    IArtMethodAdapter* target_method;
    const char* s_target_func_name;
    const char* s_target_func_signature;
    uint32_t old_entry_point_from_quick_compiled_code;

    IArtMethodAdapter* proxy_method;
    const char* s_proxy_func_name;
    const char* s_proxy_func_signature;

    IArtMethodAdapter* pre_method;
    const char* s_pre_func_name;
    const char* s_pre_func_signature;

    IArtMethodAdapter* post_method;
    const char* s_post_func_name;
    const char* s_post_func_signature;
};

map<uint32_t,HookInfo*> hook_func_mapping;

HookInfo* findHookInfo(uint32_t target){

    for(map<uint32_t,HookInfo*>::iterator itr = hook_func_mapping.begin();itr!=hook_func_mapping.end();itr++){
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

    HookInfo* pInfo = findHookInfo(r0);
    if(pInfo == NULL){
        LOGD("damn,hook info lost.");
        return;
    }

    if(pInfo->pre_method != NULL){
        LOGD("call preMethod[%x]: %s %s",pInfo->pre_method->getEntry2QuickCode(),pInfo->s_pre_func_name,pInfo->s_pre_func_signature);
        call_art_method(
            pInfo->pre_method->getMethodAddress(),
            r1,r2,r3,r4,r5,r6,r7,
            oldSP,fArgs,
            pInfo->pre_method->getEntry2QuickCode());
    }

    if(pInfo->proxy_method != NULL){
        LOGD("call proxyMethod[%x]: %s %s",pInfo->proxy_method->getEntry2QuickCode(),pInfo->s_proxy_func_name,pInfo->s_proxy_func_signature);
        call_art_method(
            pInfo->proxy_method->getMethodAddress(),
            r1,r2,r3,r4,r5,r6,r7,
            oldSP,fArgs,
            pInfo->proxy_method->getEntry2QuickCode());
    }else{
        LOGD("call targetMethod[%x]: %s %s",pInfo->target_method->getEntry2QuickCode(),pInfo->s_target_func_name,pInfo->s_target_func_signature);
        call_art_method(
            r0,r1,r2,r3,r4,r5,r6,r7,
            oldSP,fArgs,
            pInfo->old_entry_point_from_quick_compiled_code);
    }

    if(pInfo->post_method != NULL){
        LOGD("call postMethod[%x]: %s %s",pInfo->post_method->getEntry2QuickCode(),pInfo->s_post_func_name,pInfo->s_post_func_signature);
        call_art_method(
            pInfo->post_method->getMethodAddress(),
            r1,r2,r3,r4,r5,r6,r7,
            oldSP,fArgs,
            pInfo->post_method->getEntry2QuickCode());
    }
}

extern "C" JNIEXPORT jint JNICALL
Java_example_chedifier_hook_hook_Hook_hookMethodNative(JNIEnv *env,
                                                       jclass type,
                                                       jclass c,jstring name,jstring sig,jboolean isStatic,
                                                       jclass proxyC,jstring proxyName,jstring proxySig,jboolean isProxyStatic,
                                                       jint hookType) {

    Utils::logoutABI();

    if(!ArtMethodHelper::isCurrentArtVersionSupport(env)){
        LOGD("not support current art version.");
        return 1;
    }

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

    IArtMethodAdapter* targetArtMethod = ArtMethodHelper::createArtMethodAdapter(env,targetMethod);
    if(targetArtMethod == NULL){
        LOGD("adapt target method failed.");
        return 1;
    }
    targetArtMethod->print();

    IArtMethodAdapter* proxyArtMethod = ArtMethodHelper::createArtMethodAdapter(env,proxyMethod);
    if(proxyArtMethod == NULL){
        LOGD("adapt proxy method failed.");
        return 1;
    }
    proxyArtMethod->print();

    LOGD("replace code entry >>>");

    uint32_t old_entry_point_from_quick_compiled_code = targetArtMethod->getEntry2QuickCode();
    targetArtMethod->setEntry2QuickCode(reinterpret_cast<uint32_t>(art_method_handler));

    LOGD("old_entry_point_from_quick_compiled_code [%x]\n",old_entry_point_from_quick_compiled_code);
    LOGD("art_method_handler [%p]\n",art_method_handler);

    struct HookInfo *pInfo = findHookInfo(reinterpret_cast<uint32_t>(targetMethod));
    if(pInfo == NULL){
        pInfo = new HookInfo();
        pInfo->target_method = targetArtMethod;
        pInfo->s_target_func_name = target_method_name;
        pInfo->s_target_func_signature = target_method_signature;
        pInfo->old_entry_point_from_quick_compiled_code = old_entry_point_from_quick_compiled_code;


        hook_func_mapping.insert(pair<uint32_t,HookInfo*>(reinterpret_cast<uint32_t>(targetMethod),pInfo));
    }

    LOGD("hookType: %d",hookType);
    switch(hookType){
        case REPLACE_TARGET:{
            pInfo->proxy_method = proxyArtMethod;
            pInfo->s_proxy_func_name = proxy_method_name;
            pInfo->s_proxy_func_signature = proxy_method_signature;
            break;
        }
        case POST_TARGET:{
            pInfo->post_method = proxyArtMethod;
            pInfo->s_post_func_name = proxy_method_name;
            pInfo->s_post_func_signature = proxy_method_signature;
            break;
        }

        default:
        case BEFORE_TARGET:{
            pInfo->pre_method = proxyArtMethod;
            pInfo->s_pre_func_name = proxy_method_name;
            pInfo->s_pre_func_signature = proxy_method_signature;
            break;
        }
    }

    return 0;
}

