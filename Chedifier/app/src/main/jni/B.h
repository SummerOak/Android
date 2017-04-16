//
// Created by Administrator on 2016/12/13.
//

#ifndef CHEDIFIER_B_H
#define CHEDIFIER_B_H


#include <stddef.h>
#include <string.h>
#include "A.h"
#include "PureVirtualV.h"
#include "common.h"

class B :public PureVirtualV{

public:

    B(){
        LOGD("construct B");
//        memset(pChar,0,sizeof(pChar));
    }

    ~B(){
        LOGD("destruct B");
    }

    virtual void test(){
        LOGD("test");
    }
    void func();


    void call_memcopy(char* target){

        while(1){
            LOGD("call_memcopy");
            char* tmp = new char[5000];
            LOGD("call_memcopy >> %x",tmp);
            memcpy(tmp, target, 5000);
            LOGD("call_memcopy end");
        }

    }

private:
    A* pA = NULL;
    int a = 0;
    char* pChar = (char*)0x6;
};


#endif //CHEDIFIER_B_H
