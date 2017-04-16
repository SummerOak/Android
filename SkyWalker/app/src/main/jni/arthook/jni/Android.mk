LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := chedifier_art_method
NDK_APP_DST_DIR := ../jniLibs/$(TARGET_ARCH_ABI)

LOCAL_CFLAGS := -std=c++11

LOCAL_SRC_FILES := ../chedifier_hook_lib.cpp
LOCAL_SRC_FILES += $(wildcard $(LOCAL_PATH)/../../util/*.cpp)
LOCAL_SRC_FILES += $(wildcard $(LOCAL_PATH)/../target/v23/*.cpp)
LOCAL_SRC_FILES += ../aarch64.S

LOCAL_C_INCLUDES += $(LOCAL_PATH)/..
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../adapter
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../target
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../target/v23
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../util
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../common

LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

