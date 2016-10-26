//
// Created by Administrator on 2016/5/14.
//

#ifndef HOOK_ARTMETHOD_H
#define HOOK_ARTMETHOD_H

#include <type_traits>

#include "common.h"
#include "define.h"

#include "Offset.h"


class ArtMethod_6_0 {

public:

    void print();
    /*void clearAccessFlag(uint32_t flag);
    void setAccessFlag(uint32_t flag);
    void SetEntryPointFromJni(const void* entrypoint);
    void SetEntryPointFromJniPtrSize(const void* entrypoint, size_t pointer_size);

    template<typename T>
    void SetEntryPoint(MemberOffset offset, T new_value, size_t pointer_size) ;

    void setInterpreterCodeEntry(const void* entry);
    void setCompiledCodeEntry(const void* entry);

    template<typename T>
    static constexpr bool IsPowerOfTwo(T x) ;

    template<typename T>
    static T RoundDown(T x, typename std::tr1::decay<T>::type n) ;

    template<typename T>
    static T RoundUp(T x, typename std::tr1::remove_reference<T>::type n) ;

    size_t PtrSizedFieldsOffset(size_t pointer_size) ;
    MemberOffset EntryPointFromJniOffset(size_t pointer_size) ;
    MemberOffset EntryPointFromQuickCompiledCodeOffset(size_t pointer_size) ;
    MemberOffset EntryPointFromInterpreterOffset(size_t pointer_size) ;
*/
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
