//
// Created by Administrator on 2016/5/14.
//

#include <jni.h>
#include "StringUtils.h"

std::string StringUtils::ConvertJString2String (JNIEnv *env, jstring str) {

    if ( !str ) return "";

    const jsize len = env->GetStringUTFLength(str);
    const char* strChars = env->GetStringUTFChars(str, (jboolean *)0);
    std::string rlt(strChars, len);
    env->ReleaseStringUTFChars(str, strChars);

    return rlt;
}

const char* StringUtils::ConvertJString2CStr (JNIEnv *env, jstring str) {

    if ( !str )
        return "";

    const char* strChars = env->GetStringUTFChars(str, (jboolean *)0);

    return strChars;
}