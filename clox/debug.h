#ifndef __CLOX_DEBUG_H_
#define __CLOX_DEBUG_H_

#include "chunk.h"

void disassembleChunk(Chunk *chunk, const char *name);
size_t disassembleInstruction(Chunk *chunk, size_t offset);

#endif // __CLOX_DEBUG_H_
