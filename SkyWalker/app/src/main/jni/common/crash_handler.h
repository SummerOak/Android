//
// Created by Administrator on 2016/11/2.
//

#ifndef HOOK_CRASH_HANDLER_H
#define HOOK_CRASH_HANDLER_H

#include <sstream>
#include <iomanip>
#include <unwind.h>
#include <dlfcn.h>
#include <stdlib.h>

#include "common.h"

struct BacktraceState
{
    void** current;
    void** end;
};

_Unwind_Reason_Code unwindCallback(struct _Unwind_Context* context, void* arg)
{
    BacktraceState* state = static_cast<BacktraceState*>(arg);
    uintptr_t pc = _Unwind_GetIP(context);
    if (pc) {
        if (state->current == state->end) {
            return _URC_END_OF_STACK;
        } else {
            *state->current++ = reinterpret_cast<void*>(pc);
        }
    }
    return _URC_NO_REASON;
}

size_t captureBacktrace(void** buffer, size_t max)
{
    BacktraceState state = {buffer, buffer + max};
    _Unwind_Backtrace(unwindCallback, &state);

    return state.current - buffer;
}

void dumpBacktrace(std::ostream& os, void** buffer, size_t count)
{
    for (size_t idx = 0; idx < count; ++idx) {
        const void* addr = buffer[idx];
        const char* symbol = "";

        Dl_info info;
        if (dladdr(addr, &info) && info.dli_sname) {
            symbol = info.dli_sname;
        }

        os << "  #" << std::setw(2) << idx << ": " << addr << "  " << symbol << "\n";
    }
}

void backtraceToLogcat()
{
    const size_t max = 800;
    void* buffer[max];
    std::ostringstream oss;

    dumpBacktrace(oss, buffer, captureBacktrace(buffer, max));

    __android_log_print(ANDROID_LOG_INFO, "app_name", "%s", oss.str().c_str());
}

void signal_handler(int s){
    LOGD("handler [%d]>>>>>>>>>>>>>>>>>>>>",s);

    backtraceToLogcat();

    exit(EXIT_FAILURE);
}

void init_signal_handler(){

    struct sigaction sigIntHandler;
    sigIntHandler.sa_handler = signal_handler;
    sigemptyset(&sigIntHandler.sa_mask);
    sigIntHandler.sa_flags = 0;
    sigaction(SIGINT, &sigIntHandler, NULL);
    sigaction(SIGSEGV, &sigIntHandler, NULL);

}

#endif //HOOK_CRASH_HANDLER_H
