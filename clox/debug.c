#include <stdio.h>

#include "debug.h"
#include "value.h"

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

static size_t constantInstruction(const char *name, Chunk *chunk,
                                  size_t offset) {
    uint8_t constant = chunk->code[offset + 1];
    printf("%-16s 0x%02hhX '", name, constant);
    if(constant < chunk->constants.count) {
        printValue(chunk->constants.values[constant]);
    } else {
        printf("<MISSING>");
    }
    puts("'");

    return offset + 2;
}

size_t disassembleInstruction(Chunk *chunk, size_t offset) {
    printf("%04zd ", offset);
    int line = getLine(chunk, offset);
    if(offset > 0 && line == getLine(chunk, offset - 1)) {
        printf("   | ");
    } else {
        printf("%4d ", line);
    }

    OpCode opcode = (OpCode)chunk->code[offset];
    switch(opcode) {
        case OP_CONSTANT:
            return constantInstruction("OP_CONSTANT", chunk, offset);
        case OP_RETURN:
            return simpleInstruction("OP_RETURN", offset);
        default:
            printf("UNKNOWN 0x%02hhX\n", (uint8_t)opcode);
            return offset + 1;
    }
}
