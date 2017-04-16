#ifndef __UC_TRANSIENT_MEMORY_POOL_H_
#define __UC_TRANSIENT_MEMORY_POOL_H_

#define UC_TR_MEMORY_SEGMENT_SHIFT   11      // segment unit: 2048 (1 << 11)

#define NDEBUG
#define UC_INT32 unsigned int
#define UC_BOOL bool

#define IMPLE_OBJECT_GET_INSTANCE(type) \
static type * g_object = NULL; \
static void destroy(void *  paObject) \
{ \
    type * psObject = (type*)(paObject); \
    delete psObject; \
    g_object = NULL; \
} \
\
type* type::getInstance() \
{ \
    if (g_object == NULL) \
    { \
        g_object= new type(); \
    } \
 \
    return g_object; \
}

// NOTE: 
enum TransientMemoryPoolType_e
{
    E_TRMEM_POOL_UNKNOWN    = 1,
    E_TRMEM_POOL_START      = 2,        
    E_TRMEM_POOL_BYTE_4     = 2,        // the number of shift operation
    E_TRMEM_POOL_BYTE_8,
    E_TRMEM_POOL_BYTE_16,
    E_TRMEM_POOL_BYTE_32,
    E_TRMEM_POOL_BYTE_64,
    E_TRMEM_POOL_BYTE_128,
    E_TRMEM_POOL_BYTE_256,
    E_TRMEM_POOL_BYTE_512,
    E_TRMEM_POOL_BYTE_1024,
    E_TRMEM_POOL_BYTE_2048,
    //E_TRMEM_POOL_BYTE_4096,

    //NOTE: please add new type above this
    E_TRMEM_POOL_END,
};

class UcTransientMemoryPoolInfo_c
{
    friend class UcTransientMemoryPool_c;

    public:
        UcTransientMemoryPoolInfo_c();
        ~UcTransientMemoryPoolInfo_c();

        void setMemoryPoolInfo(char *pPool, int nStartSeg, int nSegNum, TransientMemoryPoolType_e ePoolType);

        void* getTrMemBlock(int& nIndex);
        void freeTrMemBlock(void* pPtr);

        inline UC_BOOL isAvaiable()    { return (NULL != pTrPoolPtr_m) && (nBlockNumber_m > 0); }
        inline UC_BOOL includeSegment(int nSegIndex)
        {
            return ((nStartSegment_m <= nSegIndex) && (nSegIndex < nStartSegment_m + nSegmentNumber_m));
        }
    protected:
    private:
        char *pTrPoolPtr_m;               // set by Memory pool, never free it here

        int nStartSegment_m;
        int nSegmentNumber_m;

        int nBlockNumber_m;
        int nUsageFlagLen_m;
        // block usage flag:
        //  0: Free
        //  1: Occupied
        char* pBlockUsageFlag_m;

        TransientMemoryPoolType_e ePoolType_m;
};

class UcTransientMemoryPool_c
{
    public:
        ~UcTransientMemoryPool_c();

        static UcTransientMemoryPool_c* getInstance();

        void* trAlloc(unsigned int nSize);
        void* trResize(void* pPtr, int nOrigSize, int nNewSize);
        void trFree(void* pPtr);

        int getPtrSize(void* ptr);
    protected:
        UcTransientMemoryPool_c();

        inline void initSegmentDefaultAssignment()
        {
            // Adjust nShiftWidth to change pool segment allocation
            int nShiftWidth = 4;

            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_4]     =  0;//2 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_8]     =  0;//2 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_16]    =  0;//2 << nShiftWidth;

            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_32]    =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_64]    =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_128]   =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_256]   =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_512]   =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_1024]  =  1 << nShiftWidth;
            nDefaultSegNum_m[E_TRMEM_POOL_BYTE_2048]  =  1 << nShiftWidth;
        }

        int getBlockIndex(void * pPtr, TransientMemoryPoolType_e& ePoolType);
    private:


        // for debug purpose
        static int nOccupiedBlockCount_m;

        // One big pool, and to be splitted into fixed, small size block
        //
        // Number of Byte  * Number of Blocks
        //       4         *        512                 // 2048
        //       8         *        256                 // 2048
        //       16        *        128                 // 2048
        //
        //       32        *        64                  // 2048
        //       64        *        32                  // 2048
        //       128       *        16                  // 2048
        //       256       *        8                   // 2048
        //       512       *        4                   // 2048
        //       1024      *        2                   // 2048
        //       2048      *        1                   // 2048
        char* pTrPool_m;

        UcTransientMemoryPoolInfo_c trmSegPool_m[E_TRMEM_POOL_END];
        // This array keeps the default segment number of each pool
        //  .) The segment number of each pool MUST be 2^N, AND N >= 1
        int nDefaultSegNum_m[E_TRMEM_POOL_END];

        // the total segment number (1024 byte)
        int nMaxSegmentNumber_m;        
        // keep the mapping between memory block(index) and user instance
};

#endif

