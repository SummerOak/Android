#ifndef H_TRACE_DEF
#define H_TRACE_DEF

#include <string.h>
#include <ctype.h>
#include <fcntl.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <dlfcn.h>
#include <elf.h>
#include <unistd.h>
#include <errno.h>
#include <sys/mman.h>

//#define __NR_write 4
#define LONG_SIZE sizeof(long)
#define CPSR_T_MASK     ( 1u << 5 )

#endif