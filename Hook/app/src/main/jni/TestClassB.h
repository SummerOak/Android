//
// Created by Administrator on 2016/5/13.
//

#ifndef HOOK_TESTCLASSB_H
#define HOOK_TESTCLASSB_H

#include <string>

class TestClassB {

public:

    TestClassB(const char* val){
        mVal = val;
    }


    std::string mVal;
    int mIntVal;
    std::string mVal2;
    std::string mValExt = "ext";


    void print();

private:
};


#endif //HOOK_TESTCLASSB_H
