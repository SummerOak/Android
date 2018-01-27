//
// Created by Administrator on 2016/10/10.
//

#ifndef HOOK_UTILS_H
#define HOOK_UTILS_H



class Utils {

public:

    static void logoutABI();

    static float unsigned_int2_float(unsigned int x);

    static int getSystemAPLevel(JNIEnv *env);
};



#endif //HOOK_UTILS_H
