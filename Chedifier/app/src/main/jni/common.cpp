//
// Created by Administrator on 2017/2/27.
//

#include <common.h>
#include <stddef.h>
#include <stdlib.h>
#include "common.h"

void * operator new(size_t s)
{
    LOGD("new %d",s);
    void * p = malloc(s);
    if (!p) {
        LOGD("malloc ret NULL");
    }
    return p;
}


