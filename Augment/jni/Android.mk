LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := BlackBox
LOCAL_SRC_FILES := BlackBox.cpp

include $(BUILD_SHARED_LIBRARY)