//
// Created by Administrator on 2016/10/24.
//

#ifndef HOOK_IARTMETHODADAPTER_H
#define HOOK_IARTMETHODADAPTER_H

#include <stdint.h>

class IArtMethodAdapter {

    public:

        virtual uint32_t getMethodAddress() = 0;
        virtual void setEntry2QuickCode(uint32_t) = 0;
        virtual uint32_t getEntry2QuickCode() = 0;
        virtual void print() = 0;

};



#endif //HOOK_IARTMETHODADAPTER_H
