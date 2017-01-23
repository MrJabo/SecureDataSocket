BIN :=./bin/
JFLAGS := -d $(BIN) -g
JC := javac
SRC := ./src/cryptolib
TEST_SRC := ./test
SOURCES := $(shell find $(SRC) -name '*.java')
TEST_SOURCES := $(shell find $(TEST_SRC) -name '*.java')
JAR :=crytolib.jar
JARFILE := $(shell find . -maxdepth 1 -name '*.jar')
BINFILES := $(shell find $(BIN) -name '*.class')
CLASSFILES := $(shell cd $(BIN) && find . -name '*.class')
.SUFFIXES: .java .class

.PHONY: all crytolib test

all: cryptolib test
cryptolib:
	$(JC) -classpath $(JARFILE) $(JFLAGS) $(SOURCES)

test:
	$(JC) -classpath $(JARFILE) -classpath $(BIN) $(JFLAGS) $(TEST_SOURCES)

clean:
	$(RM) -r $(BIN) && \
	mkdir $(BIN)

jar: clean cryptolib $(classes)
	cd $(BIN) && \
	unzip ../$(JARFILE) && \
	jar cvf $(JAR) -C . .
