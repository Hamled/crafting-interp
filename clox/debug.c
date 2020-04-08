#include <stdio.h>

#include "debug.h"

void disassembleChunk(Chunk *chunk, const char *name) {
    printf("== %s ==\n", name);

    for(size_t offset = 0; offset < chunk->count;) {
        offset = disassembleInstruction(chunk, offset);
    }
}

static size_t simpleInstruction(const char *name, size_t offset) {
    puts(name);
    return offset + 1;
}

size_t disassembleInstruction(Chunk *chunk, size_t offset) {
    printf("%04zd ", offset);

    OpCode opcode = (OpCode)chunk->code[offset];
    switch(opcode) {
        case OP_RETURN:
            return simpleInstruction("OP_RETURN", offset);
        default:
            printf("UNKNOWN 0x%02hhX\n", (uint8_t)opcode);
            return offset + 1;
    }
}
