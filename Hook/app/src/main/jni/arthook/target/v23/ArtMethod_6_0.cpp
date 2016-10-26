//
// Created by Administrator on 2016/5/14.
//

#include "ArtMethod_6_0.h"
void ArtMethod_6_0:: print(){
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

/*
ArtMethod_6_0::void clearAccessFlag(uint32_t flag){
    access_flags_ &= (~flag);
}

ArtMethod_6_0::void setAccessFlag(uint32_t flag){
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
}*/

