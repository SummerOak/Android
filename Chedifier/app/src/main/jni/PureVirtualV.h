//
// Created by Administrator on 2017/2/6.
//

#ifndef CHEDIFIER_PUREVIRTUALV_H
#define CHEDIFIER_PUREVIRTUALV_H


#include "common.h"

class PureVirtualV {
public:
    PureVirtualV(){
        LOGD("construct PureVirtualV");
//        test();
    }

    ~PureVirtualV(){
        LOGD("destruct PureVirtualV");
//        test();
    }

    virtual void test() = 0;
};


#endif //CHEDIFIER_PUREVIRTUALV_H
