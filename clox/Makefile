##
# Clox
#
# @file
# @version 0.1

CC=clang
CFLAGS=-std=c17 -Wall -Werror $(EXTRA_CFLAGS)
LDFLAGS=-fuse-ld=lld


SOURCES := $(wildcard *.c)
OBJS := $(SOURCES:.c=.o)

clox: $(OBJS)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $@ $^

%.o:

all: clox

clean:
	rm -f clox $(OBJS)

.PHONY: clean
# end
