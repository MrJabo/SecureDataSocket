# This file is part of SecureDataSocket
# Copyright (C) 2017 Jakob Bode and Matthias Sekul
#
# SecureDataSocket is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# SecureDataSocket is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with SecureDataSocket.  If not, see <http://www.gnu.org/licenses/>

BIN :=./bin/
JFLAGS := -d $(BIN) -g
JC := javac
SRC := ./src/main/java/com/cryptolib
TEST_SRC := ./src/test/java/com/cryptolib
SOURCES := $(shell find $(SRC) -name '*.java')
TEST_SOURCES := $(shell find $(TEST_SRC) -name '*.java')
JAR :=cryptolib.jar
JARFILE := $(shell find . -maxdepth 1 -name '*.jar')
BINFILES := $(shell find $(BIN) -name '*.class')
CLASSFILES := $(shell cd $(BIN) && find . -name '*.class')

.SUFFIXES: .java .class

.PHONY: all crytolib test

all: check_dep cryptolib test

check_dep:
ifndef JARFILE
	@echo "Please copy the latest .jar bouncy castle lib into $(PWD)"
	@echo "You can find the latest lib at https://www.bouncycastle.org/latest_releases.html"
	@echo "e.g. wget \"https://downloads.bouncycastle.org/java/bcprov-jdk15on-156.jar\""
	exit 1
endif

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
