#ifndef __CLOX_CHUNK_H_
#define __CLOX_CHUNK_H_

#include "common.h"

typedef enum {
    OP_RETURN,
} OpCode;

typedef struct {
    size_t count;
    size_t capacity;
    uint8_t *code;
} Chunk;

#endif // __CLOX_CHUNK_H_
