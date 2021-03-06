#include <stdlib.h>

#include "common.h"
#include "chunk.h"
#include "debug.h"
#include "vm.h"

int main(int argc, const char* argv[]) {
    Chunk chunk;
    initChunk(&chunk);
    initVM();

    size_t constant = addConstant(&chunk, 1.2);
    writeChunk(&chunk, OP_CONSTANT, 123);
    writeChunk(&chunk, constant, 123);

    writeChunk(&chunk, OP_RETURN, 123);
    writeChunk(&chunk, 123, 124);

    interpret(&chunk);

    freeVM();
    freeChunk(&chunk);
    return EXIT_SUCCESS;
}
