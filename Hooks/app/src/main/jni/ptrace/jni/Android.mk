LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := chedifier_ptrace
NDK_APP_DST_DIR := ../jniLibs/$(TARGET_ARCH_ABI)

LOCAL_SRC_FILES := ../ptrace_hook.c

LOCAL_C_INCLUDES += ./common
LOCAL_C_INCLUDES += ./ptrace

LOCAL_CFLAGS += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE -shared

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog -lm -lz

include $(BUILD_SHARED_LIBRARY)