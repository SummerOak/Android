//
// Created by Administrator on 2016/5/13.
//

#ifndef HOOK_TESTCLASSA_H
#define HOOK_TESTCLASSA_H

#include <string>
#include "define.h"

class TestClassA {

public:

    TestClassA(const char* val){
        mVal = val;
    }

    std::string mVal;

    static void empty1(){}

    int mIntVal;

    void empty2(){}
    std::string mVal2;


    void print();

private:

};


#endif //HOOK_TESTCLASSA_H
