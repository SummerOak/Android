//
// Created by Administrator on 2016/10/26.
//

#ifndef HOOK_ARTMETHODHELPER_H
#define HOOK_ARTMETHODHELPER_H

#include <jni.h>
#include "Utils.h"
#include "ArtMethodAdapter_6_0.h"

class ArtMethodHelper {

    public:

    static IArtMethodAdapter* createArtMethodAdapter(JNIEnv* env,jmethodID methodID){
        int apiLevel = Utils::getSystemAPLevel(env);
        IArtMethodAdapter* ret = NULL;
        switch(apiLevel){
        case 23:{
            ret = new ArtMethodAdapter_6_0(methodID);
            break;
        }

        }

        return ret;
    }

    static bool isCurrentArtVersionSupport(JNIEnv* env){
        switch(Utils::getSystemAPLevel(env)){
            case 23:{
               return true;
            }

            }

        return false;
    }

};



#endif //HOOK_ARTMETHODHELPER_H
