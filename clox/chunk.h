#ifndef __CLOX_CHUNK_H_
#define __CLOX_CHUNK_H_

#include "common.h"
#include "value.h"

typedef enum {
    OP_CONSTANT,
    OP_RETURN,
} OpCode;

typedef struct {
    int line;
    size_t offset;
} LineMapEntry;

typedef struct {
    size_t count;
    size_t capacity;
    LineMapEntry *entries;
} LineMap;

typedef struct {
    size_t count;
    size_t capacity;
    uint8_t *code;
    LineMap lineMap;
    ValueArray constants;
} Chunk;

void initChunk(Chunk *chunk);
void freeChunk(Chunk *chunk);
void writeChunk(Chunk *chunk, uint8_t byte, int line);
size_t addConstant(Chunk *chunk, Value value);
int getLine(Chunk *chunk, size_t offset);

#endif // __CLOX_CHUNK_H_
