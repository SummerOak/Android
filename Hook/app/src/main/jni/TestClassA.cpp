//
// Created by Administrator on 2016/5/13.
//

#include "TestClassA.h"

void TestClassA:: print(){
    LOGD("mVal: %s mIntVal: %d  mVal2: %s",
         mVal.c_str(),
        mIntVal,
    mVal2.c_str());
}