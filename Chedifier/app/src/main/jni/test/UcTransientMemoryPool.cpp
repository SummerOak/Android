#include <stddef.h>
#include "UcTransientMemoryPool.h"

#pragma clang diagnostic push
#pragma ide diagnostic ignored "CannotResolve"

#include <stdlib.h>
#include "../common.h"

#define MSG_TRMEM LOGD

////////////////////////////////////////////////////////////////////////////////
// external interface: interface/baseos/UcTransientMemory.h
////////////////////////////////////////////////////////////////////////////////
//

// WARNING: allocation from UC_TR_MEMORY_POOL is NOT thread-safe!!!!!
void* UC_TR_MEMORY_ALLOC(UC_INT32 size)
{
    return UcTransientMemoryPool_c::getInstance()->trAlloc(size);
}

void* UC_TR_MEMORY_REALLOC(void* ptr, UC_INT32 nOrigSize, UC_INT32 nNewSize)
{
    return UcTransientMemoryPool_c::getInstance()->trResize(ptr, nOrigSize, nNewSize);
}

void UC_TR_MEMORY_FREE(void* ptr)
{
    UcTransientMemoryPool_c::getInstance()->trFree(ptr);
}
//
////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////
// memory pool implementation
////////////////////////////////////////////////////////////////////////////////
//
int UcTransientMemoryPool_c::nOccupiedBlockCount_m = 0;;

UcTransientMemoryPoolInfo_c::UcTransientMemoryPoolInfo_c() 
{
    pTrPoolPtr_m        = NULL;
    nStartSegment_m     = -1;
    nSegmentNumber_m    = 0;
    nBlockNumber_m      = -1;
    nUsageFlagLen_m     = -1;
    pBlockUsageFlag_m   = NULL;
    ePoolType_m         = E_TRMEM_POOL_UNKNOWN;
}

UcTransientMemoryPoolInfo_c::~UcTransientMemoryPoolInfo_c() 
{
    if (NULL != pBlockUsageFlag_m)
    {
        delete pBlockUsageFlag_m;
        pBlockUsageFlag_m    = NULL;
    }
}

void UcTransientMemoryPoolInfo_c::setMemoryPoolInfo(char *pPool, int nStartSeg, int nSegNum, TransientMemoryPoolType_e ePoolType)
{
    pTrPoolPtr_m        = pPool;
    nStartSegment_m     = nStartSeg;
    nSegmentNumber_m    = nSegNum;
    ePoolType_m         = ePoolType;

    // get the number of character needed to keep usage flags for each block
    //
    nBlockNumber_m      = ((((0x01 << UC_TR_MEMORY_SEGMENT_SHIFT) - 1) >> ePoolType_m) + 1) * nSegNum;

    MSG_TRMEM("Pool init: type [%d] block [%d]\n", (0x01 << ePoolType_m), nBlockNumber_m);

    // bytes needed for keeping usage flag
    nUsageFlagLen_m     = nBlockNumber_m >> 3;
    if (nBlockNumber_m & 0x07)
    {
        nUsageFlagLen_m ++;
    }

    pBlockUsageFlag_m   = new char [nUsageFlagLen_m];
    if (NULL == pBlockUsageFlag_m)
    {
        return;
    }

    memset(pBlockUsageFlag_m, 0, nUsageFlagLen_m);

    // set the last us-used flag as 1 (occupied)
    if (nBlockNumber_m & 0x07)
    {
        for (int nInd=0; nInd<(0x08 - (nBlockNumber_m & 0x07)); nInd++)
        {
            pBlockUsageFlag_m[nUsageFlagLen_m-1] |= (0x80 >> nInd);
        }
    }

    return;
}

////////////////////////////////////////////////////////////////////////////////
//
//         Bit:      7   6   5   4   3   2   1   0
//
// Block Index:   7 + n*8                     0 + n*8
//                 ---------------------------------
//  *Flags: n=0    |   |   |   |   |   |   |   |   |
//                 ---------------------------------
//          n=1    |   |   |   |   |   |   |   |   |
//                 ---------------------------------
//                         ...
//                 ---------------------------------
//          n=N    |   |   |   |   |   |   |   |   |
//                 ---------------------------------
//
////////////////////////////////////////////////////////////////////////////////
void* UcTransientMemoryPoolInfo_c::getTrMemBlock(int& nIndex)
{
    int nFlagIndexbyInt = 0;
    UC_BOOL bFoundAvailable = false;

    int* pFlagByInt = (int*)pBlockUsageFlag_m;
    char* pFlagByChar = pBlockUsageFlag_m;

    // search free block by integer first
    for (nFlagIndexbyInt=0; nFlagIndexbyInt<(nBlockNumber_m >> 5); nFlagIndexbyInt++)
    {
        if (0xFFFFFFFF != (*pFlagByInt & 0xFFFFFFFF))
        {
            // some blocks are available...
            bFoundAvailable = true;
            pFlagByChar     = (char*) pFlagByChar;

            break;
        }
        pFlagByInt ++;
    }

    int nByteCheckNum   = 4;
    if (false == bFoundAvailable) 
    {
        if (nUsageFlagLen_m & 0x03)
        {
            nByteCheckNum = (nUsageFlagLen_m & 0x03);
        }
        else
        {
            return NULL;
        }
    }

    // Get char index
    int nFlagCharOffset     = (nFlagIndexbyInt << 2);
    int nFlagCharCheckEnd   = nFlagCharOffset + nByteCheckNum;
    for (; nFlagCharOffset < nFlagCharCheckEnd; nFlagCharOffset++)
    {
        if (0x0FF != (pBlockUsageFlag_m[nFlagCharOffset] & 0x0FF))
        {
            bFoundAvailable = true;
            break;
        }
    }

    // get bit index
    if ( bFoundAvailable) 
    {
        char cBitMask = 0x01;
        for (int nBitInd=0; nBitInd<8; nBitInd++)
        {
            if (0 == (pBlockUsageFlag_m[nFlagCharOffset] & cBitMask))
            {
                // Set the flag as occupied
                pBlockUsageFlag_m[nFlagCharOffset] |= cBitMask;

                // Available block is found
                nIndex = (nFlagCharOffset << 3) + nBitInd;

                return (pTrPoolPtr_m + (nIndex << ePoolType_m));
            }

            // shift bit mask
            cBitMask <<= 1;
        }
    }

    return NULL;
}

void UcTransientMemoryPoolInfo_c::freeTrMemBlock(void* pPtr)
{
    int pPtrOffset      = (char*)pPtr - pTrPoolPtr_m;

    // From performance perspective, this check may not need... 
    if ((pPtrOffset < 0) || (pPtrOffset >= (nSegmentNumber_m << UC_TR_MEMORY_SEGMENT_SHIFT)))
    {
        MSG_TRMEM("Pointer is out of scope\n");
        return;
    }

    int nBlockIndex = (pPtrOffset >> ePoolType_m);

    // Reset corresponding bit to FREE (0)
    unsigned char cBitMask = ~(0x01 << (nBlockIndex & 0x07));
    pBlockUsageFlag_m[(nBlockIndex >> 3)] &= cBitMask;

    return;
}



UcTransientMemoryPool_c::UcTransientMemoryPool_c()
{

    // Init the segment number of each pool
    initSegmentDefaultAssignment();

    // Get the sum of segments
    nMaxSegmentNumber_m = 0;
    for (int ePoolInd=E_TRMEM_POOL_START; ePoolInd<E_TRMEM_POOL_END; ePoolInd++)
    {
        nMaxSegmentNumber_m += nDefaultSegNum_m[ePoolInd];
    }

    // allocate whole memory for pools
    pTrPool_m = new char[nMaxSegmentNumber_m << UC_TR_MEMORY_SEGMENT_SHIFT];

    // assign the header for pools
    char* pNextHeader   = pTrPool_m;
    int nSegStart       = 0;
    for (int ePoolInd=E_TRMEM_POOL_START; ePoolInd<E_TRMEM_POOL_END; ePoolInd++)
    {
        if (nDefaultSegNum_m[ePoolInd] > 0)
        {
            trmSegPool_m[ePoolInd].setMemoryPoolInfo(pNextHeader, 
                    nSegStart, 
                    nDefaultSegNum_m[ePoolInd], 
                    (TransientMemoryPoolType_e)ePoolInd);

            pNextHeader += (trmSegPool_m[ePoolInd].nSegmentNumber_m << UC_TR_MEMORY_SEGMENT_SHIFT);
            nSegStart += nDefaultSegNum_m[ePoolInd];
        }
    }
}

UcTransientMemoryPool_c::~UcTransientMemoryPool_c()
{
    if (NULL != pTrPool_m)
    {
        delete[] (char*)pTrPool_m;
        pTrPool_m = NULL;
    }

}

IMPLE_OBJECT_GET_INSTANCE(UcTransientMemoryPool_c)

int UcTransientMemoryPool_c::getPtrSize(void* ptr)
{
    TransientMemoryPoolType_e ePoolType = E_TRMEM_POOL_UNKNOWN;
    getBlockIndex(ptr, ePoolType);

    return (E_TRMEM_POOL_UNKNOWN != ePoolType) ?  (0x01 << ePoolType) : 0;
}

int UcTransientMemoryPool_c::getBlockIndex(void * pPtr, TransientMemoryPoolType_e& ePoolType)
{
    ePoolType           = E_TRMEM_POOL_UNKNOWN;
    int pPtrOffset      = (char*)pPtr - pTrPool_m;

    if ((pPtrOffset < 0) || (pPtrOffset >= (nMaxSegmentNumber_m << UC_TR_MEMORY_SEGMENT_SHIFT)))
    {
        MSG_TRMEM("Pointer is out of scope\n");
        return -1;
    }

    int nSegmentIndex   = pPtrOffset >> UC_TR_MEMORY_SEGMENT_SHIFT;
    int nBlockIndex     = -1;

    int ePoolInd;
    for (ePoolInd=E_TRMEM_POOL_START; ePoolInd<E_TRMEM_POOL_END; ePoolInd++)
    {
        if (trmSegPool_m[ePoolInd].includeSegment(nSegmentIndex))
        {
            nBlockIndex = (pPtrOffset >> ePoolInd);
            break;
        }
    }

    if (-1 == nBlockIndex)
    {
        MSG_TRMEM("segment index is out of range\n");
    }
    else
    {
        ePoolType = (TransientMemoryPoolType_e)ePoolInd;
    }

    return nBlockIndex;
}

void* UcTransientMemoryPool_c::trAlloc(unsigned int nSize)
{
#ifndef NDEBUG
    return malloc(nSize);
#else

    if (nSize > (0x01 << (E_TRMEM_POOL_END - 1)))
    {
        nOccupiedBlockCount_m++;
        MSG_TRMEM("[%d] Too big, allocate by system directly, Total Occupied [%d]\n", nSize, nOccupiedBlockCount_m);

        return ((void*)new char[nSize]);
    }

    // lock the pool
    //mutex_m->lock();

    int nBlockIndex     = 0;
    void* pTrMemBlock   = NULL;

    for (int ePoolType=E_TRMEM_POOL_START; ePoolType<E_TRMEM_POOL_END; ePoolType++)
    {
        if ((nSize <= (unsigned int)(0x01 << ePoolType)) && ( trmSegPool_m[ePoolType].isAvaiable()))
        {
            pTrMemBlock = trmSegPool_m[ePoolType].getTrMemBlock(nBlockIndex);
            if (NULL != pTrMemBlock)
            {
                nOccupiedBlockCount_m++;

                MSG_TRMEM("Allocate [%d bytes] [%08X] block for request [%d]. Total Occupied [%d]\n", (0x01<<ePoolType), pTrMemBlock, nSize, nOccupiedBlockCount_m);
                //return pTrMemBlock;
                break;
            }
            else
            {
                MSG_TRMEM("[%d bytes] pool exhausted, try next one...\n", (0x01<<ePoolType));
            }
        }
    }

    if (NULL == pTrMemBlock)
    {
        nOccupiedBlockCount_m++;
        //MSG_TRMEM_ERR("Could not allocate from pool, try system memory for size[%d]. Total occupied [%d]\n",  nSize, nOccupiedBlockCount_m);

        pTrMemBlock =  (void*)new char[nSize];
    }

    // unlock the pool
    //mutex_m->unlock();

    return pTrMemBlock;
#endif
}

#if 0
////////////////////////////////////////////////////////////////////////////////
// WARNNING WARNNING WARNNING
//
//  Be careful to use this trResize, it can ONLY work properly when pool is big
//  enough. If system can not find original one from pool, it has no way to get
//  the memory block size, that means it will not be able to copy original content
//  to new buffer
////////////////////////////////////////////////////////////////////////////////
void* UcTransientMemoryPool_c::trResize(void* pPtr, int nNewSize)
{
    int nOrigSize = getPtrSize(pPtr);
    if ((0 >= nNewSize) || ((0 < nNewSize) && (nOrigSize >= nNewSize)))
    {
        // if new request size is smaller than original one, return original one directly
        return pPtr;
    }

    // allocate new memory block
    void* pNew = trAlloc(nNewSize);
    if ((NULL != pNew) && (nOrigSize > 0))
    {
        memcpy(pNew, pPtr, nOrigSize);
    }
    else
    {
        MSG_TRMEM_ERR("Copy orignal content error length=%d\n", nOrigSize);
    }

    // release old memory
    trFree(pPtr);

    return pNew;
}
#endif

////////////////////////////////////////////////////////////////////////////////
// foolish resize
////////////////////////////////////////////////////////////////////////////////
void* UcTransientMemoryPool_c::trResize(void* pPtr, int nOrigSize, int nNewSize)
{
#ifndef NDEBUG
    return ucrealloc(pPtr,nNewSize,nOrigSize);
#else
    if ((0 >= nNewSize) || ((0 < nNewSize) && (nOrigSize >= nNewSize)))
    {
        // if new request size is smaller than original one, return original one directly
        return pPtr;
    }

    // allocate new memory block
    void* pNew = trAlloc(nNewSize);
    if ((NULL != pNew) && (nOrigSize > 0))
    {
        memcpy(pNew, pPtr, nOrigSize);
    }
    else
    {
        MSG_TRMEM("Copy orignal content error length=%d\n", nOrigSize);
        //MSG_TRMEM_ERR("Copy orignal content error length=%d\n", nOrigSize);
    }

    // release old memory
    trFree(pPtr);

    return pNew;
#endif
}

void UcTransientMemoryPool_c::trFree(void* pPtr)
{
    if (NULL == pPtr)
    {
        return;
    }

#ifndef NDEBUG
    ucfree(pPtr);
#else

    int pPtrOffset = (char*)pPtr - pTrPool_m;
    if ((pPtrOffset < 0) || (pPtrOffset >= (nMaxSegmentNumber_m << UC_TR_MEMORY_SEGMENT_SHIFT)))
    {
        nOccupiedBlockCount_m--;

        MSG_TRMEM("delete system allocated memory. Total occupied [%d]\n", nOccupiedBlockCount_m);
        delete[] (char *)pPtr;
    }
    else
    {
        int nSegmentIndex   = pPtrOffset >> UC_TR_MEMORY_SEGMENT_SHIFT;

        //
        for (int ePoolType=E_TRMEM_POOL_START; ePoolType<E_TRMEM_POOL_END; ePoolType++)
        {
            if (trmSegPool_m[ePoolType].includeSegment(nSegmentIndex))
            {
                nOccupiedBlockCount_m--;
                MSG_TRMEM("release memory block [%d bytes] [%08X]. Total occupied [%d]\n", (0x01<<ePoolType), pPtr, nOccupiedBlockCount_m);

                trmSegPool_m[ePoolType].freeTrMemBlock(pPtr);
                break;
            }
        }
    }
#endif

    return;
}

#if 0
// Test code
int main (int argc, char* argv[])
{
    UcTransientMemoryPool_c* pTrPool = UcTransientMemoryPool_c::getInstance();

    void * pTrm[65];

    for (int i=0; i<3; i++)
    {
        MSG_TRMEM("Round %d\n", i);

        for (int j=0; j<10; j++)
        {
            pTrm[j]     = pTrPool->trAlloc(6);

            sprintf ((char*)pTrm[j],
                    "%X i=%02d, j=%02d,", //Give a length is 64 string abcdefa add",
                    (unsigned int)(pTrm[j]), i, j);
        }

        pTrm[3] = pTrPool->trResize(pTrm[3], 32, 65);
        pTrm[8] = pTrPool->trResize(pTrm[8], 32, 150);

        for (int j=10; j<13; j++)
        {
            pTrm[j]     = pTrPool->trAlloc(6);

            sprintf ((char*)pTrm[j],
                    "%X i=%02d, j=%02d,", //Give a length is 64 string abcdefa add",
                    (unsigned int)(pTrm[j]), i, j);
        }

        for (int j=0; j<13; j++)
        {
            MSG_TRMEM("%X: %s\n", (unsigned int)(pTrm[j]), (char*)pTrm[j]);
        }

        for (int j=12; j>=0; j--)
        {
            pTrPool->trFree(pTrm[j]);
        }
    }

    return 0;
}
#endif

#pragma clang diagnostic pop