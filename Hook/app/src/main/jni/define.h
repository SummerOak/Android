#ifndef DEFINE_H
#define DEFINE_H

#include <android/log.h>

#define LOG_TAG "chedifier_hook"
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

#define LIKELY(x)       __builtin_expect((x), true)
#define UNLIKELY(x)     __builtin_expect((x), false)

#define CHECK_CONSTEXPR(x, out, dummy) \
  (UNLIKELY(!(x))) ? (LOGD("Check failed: ")):

#define DCHECK_CONSTEXPR(x, out, dummy) CHECK_CONSTEXPR(x, out, dummy)

#define OFFSETOF_MEMBER(t, f) \
  (reinterpret_cast<const char*>(&reinterpret_cast<t*>(16)->f) - reinterpret_cast<const char*>(16))

#endif