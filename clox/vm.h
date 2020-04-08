#ifndef __CLOX_VM_H_
#define __CLOX_VM_H_

#include "chunk.h"

typedef struct {
    Chunk *chunk;
} VM;

void initVM();
void freeVM();

#endif // __CLOX_VM_H_
