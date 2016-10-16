//
// Created by wxj_pc on 2016/10/2.
//

#ifndef HOOK_COMMON_H_H
#define HOOK_COMMON_H_H

#include <android/log.h>

#define LOG_TAG "chedifier_hook"
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

#endif //HOOK_COMMON_H_H
