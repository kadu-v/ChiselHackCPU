SRC=..

.PHONY: init
init:
	apio init --board iCE40-HX1K-EVB

.PHONY: build
build: $(SRC)/BlackBoxSPRAM.v $(SRC)/init.bin
	cp $(SRC)/Top.v ./
	cp $(SRC)/init.bin ./
	cp $(SRC)/bin.hack ./
	apio build

.PHONY: upload
upload: hardware.bin
	apio upload

.PHONY: clean
clean:
	rm Top.v init.bin bin.hack
	rm hardware.json hardware.asc hardware.bin *.dblite