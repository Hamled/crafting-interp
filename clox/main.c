#include <stdlib.h>

#include "common.h"
#include "chunk.h"
#include "debug.h"

int main(int argc, const char* argv[]) {
    Chunk chunk;
    initChunk(&chunk);

    size_t constant = addConstant(&chunk, 1.2);
    writeChunk(&chunk, OP_CONSTANT);
    writeChunk(&chunk, constant);

    writeChunk(&chunk, OP_RETURN);
    writeChunk(&chunk, 123);

    disassembleChunk(&chunk, "test chunk");
    freeChunk(&chunk);

    return EXIT_SUCCESS;
}
