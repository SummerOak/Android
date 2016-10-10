LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := chedifierHookLib
NDK_APP_DST_DIR := ../jniLibs/$(TARGET_ARCH_ABI)

LOCAL_CFLAGS := -std=c++11

LOCAL_SRC_FILES := \
chedifier_hook_lib.cpp \
ArtMethod.cpp \
util/StringUtils.cpp \
util/Utils.cpp \
aarch64.S

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
LOCAL_C_INCLUDES += E:\private_workspace\Hook\app\src\main\jni
LOCAL_C_INCLUDES += E:\private_workspace\Hook\app\src\debug\jni

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog -lm -lz
include $(BUILD_SHARED_LIBRARY)

