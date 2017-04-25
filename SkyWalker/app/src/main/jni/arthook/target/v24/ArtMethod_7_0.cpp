//
// Created by Administrator on 2016/5/14.
//

#include "ArtMethod_7_0.h"
void ArtMethod_7_0:: print(){
    LOGD("declaring_class_ %d ",declaring_class_);
    LOGD("access_flags_ %x ",access_flags_);
    LOGD("dex_code_item_offset_ %d ",dex_code_item_offset_);
    LOGD("dex_method_index_ %d ",dex_method_index_);
    LOGD("method_index_ %d ",method_index_);
    LOGD("entry_point_from_jni_ %x ",ptr_sized_fields_.entry_point_from_jni_);
    LOGD("entry_point_from_quick_compiled_code_ %x ",ptr_sized_fields_.entry_point_from_quick_compiled_code_);
}

