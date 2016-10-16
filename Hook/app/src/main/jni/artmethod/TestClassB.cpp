//
// Created by Administrator on 2016/5/13.
//

#include "TestClassB.h"
#include "define.h"

void TestClassB:: print(){
    LOGD("mVal: %s mIntVal: %d  mVal2: %s ext: %s",
         mVal.c_str(),
         mIntVal,
         mVal2.c_str(),
        mValExt.c_str());
}