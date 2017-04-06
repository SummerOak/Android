//
// Created by Administrator on 2016/12/13.
//

#include "A.h"
#include "common.h"
#include <stddef.h>

void A::func(){

    LOGD("A call func succ");

    if(this == NULL){
        LOGD("A this is null");
    }

    LOGD("data %d",data);
}