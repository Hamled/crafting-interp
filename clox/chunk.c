#include "chunk.h"
#include "memory.h"

static void initLineMap(LineMap *map);
static void freeLineMap(LineMap *map);
static void writeLineMap(LineMap *map, int line, size_t offset);

void initChunk(Chunk *chunk) {
    chunk->count = 0;
    chunk->capacity = 0;
    chunk->code = NULL;
    initLineMap(&chunk->lineMap);
    initValueArray(&chunk->constants);
}

void freeChunk(Chunk *chunk) {
    FREE_ARRAY(chunk->code, uint8_t, chunk->capacity);
    freeLineMap(&chunk->lineMap);
    freeValueArray(&chunk->constants);
    initChunk(chunk);
}

void writeChunk(Chunk *chunk, uint8_t byte, int line) {
    if(chunk->capacity < chunk->count + 1) {
        size_t oldCapacity = chunk->capacity;
        chunk->capacity = GROW_CAPACITY(oldCapacity);
        chunk->code = GROW_ARRAY(chunk->code, uint8_t,
            oldCapacity, chunk->capacity);
    }

    chunk->code[chunk->count] = byte;
    writeLineMap(&chunk->lineMap, line, chunk->count);
    chunk->count++;
}

size_t addConstant(Chunk *chunk, Value value) {
    writeValueArray(&chunk->constants, value);
    return chunk->constants.count - 1;
}

int getLine(Chunk *chunk, size_t offset) {
    const LineMap *map = &chunk->lineMap;
    for(size_t i = 0; i < map->count; i++) {
        const LineMapEntry *entry = &map->entries[i];
        if(offset <= entry->offset) {
            return entry->line;
        }
    }

    return -1; // Somehow we didn't find this offset
}


static void initLineMap(LineMap *map) {
    map->count = 0;
    map->capacity = 0;
    map->entries = NULL;
}

static void freeLineMap(LineMap *map) {
    FREE_ARRAY(map->entries, LineMapEntry, map->capacity);
    // Don't initialize, that will be taken care of by initChunk
}

// Precondition: count <= capacity
static void writeLineMap(LineMap *map, int line, size_t offset) {
    const size_t count = map->count;
    LineMapEntry *last = NULL;
    if(count > 0) {
        last = &map->entries[count - 1];
    }

    // If there is no last entry, or it doesn't match
    // our new line num, setup a new last entry
    if(!last || last->line != line) {
        // Grow the map if necessary
        if(map->capacity < map->count + 1) {
            size_t oldCapacity = map->capacity;
            map->capacity = GROW_CAPACITY(oldCapacity);
            map->entries = GROW_ARRAY(map->entries, LineMapEntry,
                oldCapacity, map->capacity);
        }

        last = &map->entries[count];
        map->count++;
    }

    // Last entry should always have what we're writing
    last->line = line;
    last->offset = offset;
}
