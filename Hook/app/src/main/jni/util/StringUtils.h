//
// Created by Administrator on 2016/5/14.
//

#ifndef HOOK_STRINGUTILS_H
#define HOOK_STRINGUTILS_H

#include <string>

class StringUtils {

public:

    static std::string ConvertJString2String(JNIEnv* env, jstring str);

    static const char* ConvertJString2CStr(JNIEnv* env,jstring str);
};


#endif //HOOK_STRINGUTILS_H
