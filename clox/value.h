#ifndef __CLOX_VALUE_H_
#define __CLOX_VALUE_H_

#include "common.h"

typedef double Value;

typedef struct {
    size_t capacity;
    size_t count;
    Value *values;
} ValueArray;

void initValueArray(ValueArray *array);
void freeValueArray(ValueArray *array);
void writeValueArray(ValueArray *array, Value value);

#endif // __CLOX_VALUE_H_
