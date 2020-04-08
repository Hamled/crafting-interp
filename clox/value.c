#include <stdio.h>

#include "memory.h"
#include "value.h"

void initValueArray(ValueArray *array) {
    array->count = 0;
    array->capacity = 0;
    array->values = NULL;
}

void freeValueArray(ValueArray *array) {
    FREE_ARRAY(array->values, Value, array->capacity);
    initValueArray(array);
}

void writeValueArray(ValueArray *array, Value value) {
    if(array->capacity < array->count + 1) {
        size_t oldCapacity = array->capacity;
        array->capacity = GROW_CAPACITY(oldCapacity);
        array->values = GROW_ARRAY(array->values, Value,
            oldCapacity, array->capacity);
    }

    array->values[array->count] = value;
    array->count++;
}

void printValue(Value value) {
    printf("%lg", value);
}
