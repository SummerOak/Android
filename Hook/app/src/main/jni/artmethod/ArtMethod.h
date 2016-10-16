//
// Created by Administrator on 2016/5/14.
//

#ifndef HOOK_ARTMETHOD_H
#define HOOK_ARTMETHOD_H

#include <type_traits>


#include "../common/common.h"
#include "define.h"

#include "Offset.h"



class ArtMethod {

public:
    void replace(ArtMethod& v){
        this->dex_code_item_offset_ = v.dex_code_item_offset_;
        this->dex_method_index_ = v.dex_method_index_;
        this->method_index_ = v.method_index_;
        this->ptr_sized_fields_ = v.ptr_sized_fields_;

    }


    void print(){
        LOGD("declaring_class_ %d ",declaring_class_);
        LOGD("dex_cache_resolved_methods_ %d ",dex_cache_resolved_methods_);
        LOGD("dex_cache_resolved_types_ %d ",dex_cache_resolved_types_);
        LOGD("access_flags_ %x ",access_flags_);
        LOGD("dex_code_item_offset_ %d ",dex_code_item_offset_);
        LOGD("dex_method_index_ %d ",dex_method_index_);
        LOGD("method_index_ %d ",method_index_);
        LOGD("ptr_sized_fields_ %x ",ptr_sized_fields_);
        LOGD("entry_point_from_interpreter_ %x ",ptr_sized_fields_.entry_point_from_interpreter_);
        LOGD("entry_point_from_jni_ %x ",ptr_sized_fields_.entry_point_from_jni_);
        LOGD("entry_point_from_quick_compiled_code_ %x ",ptr_sized_fields_.entry_point_from_quick_compiled_code_);
    }

    void clearAccessFlag(uint32_t flag){
        access_flags_ &= (~flag);
    }

    void setAccessFlag(uint32_t flag){
        access_flags_ |= flag;
    }

    void SetEntryPointFromJni(const void* entrypoint) {
            SetEntryPointFromJniPtrSize(entrypoint, sizeof(void*));
    }

    void SetEntryPointFromJniPtrSize(const void* entrypoint, size_t pointer_size) {
        SetEntryPoint(EntryPointFromJniOffset(pointer_size), entrypoint, pointer_size);
    }

    template<typename T>
    void SetEntryPoint(MemberOffset offset, T new_value, size_t pointer_size) {
        const auto addr = reinterpret_cast<uintptr_t>(this) + offset.Uint32Value();
        if (pointer_size == sizeof(uint32_t)) {
            uintptr_t ptr = reinterpret_cast<uintptr_t>(new_value);
            *reinterpret_cast<uint32_t*>(addr) = static_cast<uint32_t>(ptr);
        } else {
            *reinterpret_cast<uint64_t*>(addr) = reinterpret_cast<uintptr_t>(new_value);
        }
    }

    void setInterpreterCodeEntry(const void* entry){
        SetEntryPoint(EntryPointFromInterpreterOffset(sizeof(void*)), entry, sizeof(void*));
    }

    void setCompiledCodeEntry(const void* entry){
        SetEntryPoint(EntryPointFromQuickCompiledCodeOffset(sizeof(void*)), entry, sizeof(void*));
    }

    template<typename T>
    static constexpr bool IsPowerOfTwo(T x) {
        static_assert(std::tr1::is_integral<T>::value, "T must be integral");
        // TODO: assert unsigned. There is currently many uses with signed values.
        return (x & (x - 1)) == 0;
    }

    template<typename T>
    static T RoundDown(T x, typename std::tr1::decay<T>::type n) {

        typename std::tr1::decay<T>::type t,b;

        return
                DCHECK_CONSTEXPR(IsPowerOfTwo(n), , T(0)) (x & -n);
    }

    template<typename T>
    static T RoundUp(T x, typename std::tr1::remove_reference<T>::type n) {
        return RoundDown(x + n - 1, n);
    }

    size_t PtrSizedFieldsOffset(size_t pointer_size) {
        // Round up to pointer size for padding field.
        return RoundUp(OFFSETOF_MEMBER(ArtMethod, ptr_sized_fields_), pointer_size);
    }

    MemberOffset EntryPointFromJniOffset(size_t pointer_size) {
        return MemberOffset(PtrSizedFieldsOffset(pointer_size) + OFFSETOF_MEMBER(PtrSizedFields, entry_point_from_jni_) / sizeof(void*) * pointer_size);
    }

    MemberOffset EntryPointFromQuickCompiledCodeOffset(size_t pointer_size) {
        return MemberOffset(PtrSizedFieldsOffset(pointer_size) + OFFSETOF_MEMBER(
                                                                         PtrSizedFields, entry_point_from_quick_compiled_code_) / sizeof(void*) * pointer_size);
    }

    MemberOffset EntryPointFromInterpreterOffset(size_t pointer_size) {
        return MemberOffset(PtrSizedFieldsOffset(pointer_size) + OFFSETOF_MEMBER(
                                                                         PtrSizedFields, entry_point_from_interpreter_) / sizeof(void*) * pointer_size);
    }

public:
    uint32_t declaring_class_;

    // Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
    uint32_t dex_cache_resolved_methods_;

    // Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
    uint32_t dex_cache_resolved_types_;

    // Access flags; low 16 bits are defined by spec.
    uint32_t access_flags_;

    /* Dex file fields. The defining dex file is available via declaring_class_->dex_cache_ */

    // Offset to the CodeItem.
    uint32_t dex_code_item_offset_;

    // Index into method_ids of the dex file associated with this method.
    uint32_t dex_method_index_;

    /* End of dex file fields. */

    // Entry within a dispatch table for this method. For static/direct methods the index is into
    // the declaringClass.directMethods, for virtual methods the vtable and for interface methods the
    // ifTable.
    uint32_t method_index_;

    // Fake padding field gets inserted here.

    // Must be the last fields in the method.
    // PACKED(4) is necessary for the correctness of
    // RoundUp(OFFSETOF_MEMBER(ArtMethod, ptr_sized_fields_), pointer_size).
    struct PtrSizedFields {
        // Method dispatch from the interpreter invokes this pointer which may cause a bridge into
        // compiled code.
        void* entry_point_from_interpreter_;

        // Pointer to JNI function registered to this method, or a function to resolve the JNI function.
        void* entry_point_from_jni_;

        // Method dispatch from quick compiled code invokes this pointer which may cause bridging into
        // the interpreter.
        void* entry_point_from_quick_compiled_code_;
    } ptr_sized_fields_;
};


#endif //HOOK_ARTMETHOD_H
