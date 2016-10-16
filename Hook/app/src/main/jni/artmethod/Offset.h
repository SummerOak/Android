//
// Created by Administrator on 2016/5/17.
//

#ifndef HOOK_OFFSET_H
#define HOOK_OFFSET_H


#include <stdint.h>

// Allow the meaning of offsets to be strongly typed.
class Offset {
public:
    explicit Offset(size_t val) : val_(val) {}
    int32_t Int32Value() const {
        return static_cast<int32_t>(val_);
    }
    uint32_t Uint32Value() const {
        return static_cast<uint32_t>(val_);
    }
    size_t SizeValue() const {
        return val_;
    }

protected:
    size_t val_;
};

// Offsets relative to an object.
class MemberOffset : public Offset {
public:
    explicit MemberOffset(size_t val) : Offset(val) {}
};


#endif //HOOK_OFFSET_H
