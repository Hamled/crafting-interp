#ifndef __CLOX_CHUNK_H_
#define __CLOX_CHUNK_H_

#include "common.h"
#include "value.h"

typedef enum {
    OP_RETURN,
} OpCode;

typedef struct {
    size_t count;
    size_t capacity;
    uint8_t *code;
    ValueArray constants;
} Chunk;

void initChunk(Chunk *chunk);
void freeChunk(Chunk *chunk);
void writeChunk(Chunk *chunk, uint8_t byte);
size_t addConstant(Chunk *chunk, Value value);

#endif // __CLOX_CHUNK_H_
