//
// Created by Administrator on 2016/10/25.
//

#ifndef HOOK_ARTMETHODADAPTER_6_0_H
#define HOOK_ARTMETHODADAPTER_6_0_H

#include <jni.h>
#include <stdint.h>
#include "ArtMethod_6_0.h"

class ArtMethodAdapter_6_0: public IArtMethodAdapter {

    public:

        uint32_t getMethodAddress(){
            return reinterpret_cast<uint32_t>(mMethod);
        }

        ArtMethodAdapter_6_0(jmethodID methodID){
            mMethod = reinterpret_cast<ArtMethod_6_0*>(methodID);
        }

        void setEntry2QuickCode(uint32_t addr){
            if(mMethod != NULL){
                mMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_ = reinterpret_cast<void*>(addr);
            }
        }

        uint32_t getEntry2QuickCode(){
            if(mMethod != NULL){
                return reinterpret_cast<uint32_t>(mMethod->ptr_sized_fields_.entry_point_from_quick_compiled_code_);
            }

            return 0;
        }

        void print(){
            if(mMethod != NULL){
                mMethod->print();
            }
        }

    private:

        ArtMethod_6_0* mMethod;

};



#endif //HOOK_ARTMETHODADAPTER_6_0_H
