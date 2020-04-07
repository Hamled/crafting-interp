##
# Clox
#
# @file
# @version 0.1

CC=clang
CFLAGS=-std=c17
LDFLAGS=-fuse-ld=lld


clox: main.o
	$(CC) $(CFLAGS) $(LDFLAGS) -o $@ $^
OUTPUTS += clox

main.o: main.c
OUTPUTS += main.o

all: clox

clean:
	rm -f $(OUTPUTS)

.PHONY: clean
# end
