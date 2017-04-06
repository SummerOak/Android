//
// Created by Administrator on 2017/3/3.
//

#ifndef CHEDIFIER_TEST_H
#define CHEDIFIER_TEST_H

#include "../common.h"

namespace Monitor{
    void func1(){
        LOGD("func1");
    }

    void func2(){
        LOGD("func2");
        func1();
    }

    void func3(){
        LOGD("func3");
        func2();
    }

    void test(){
        LOGD("test");
        func3();
    }
}

#endif //CHEDIFIER_TEST_H
