
APIO_DIR=~/.apio
YOSYS=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/yosys
NEXTPNR=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/nextpnr-ice40
ICEPACK=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/icepack
ICEPROG=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/iceprog
SRC=..

.PHONY: init
init:
	apio init --board iCE40-UP5K

.PHONY: build
build: $(SRC)/BlackBoxSPRAM.v $(SRC)/Top.v
	$(YOSYS) -p "synth_ice40 -json hardware.json" -q $(SRC)/BlackBoxSPRAM.v $(SRC)/Top.v
	$(NEXTPNR) --up5k --package sg48 --json hardware.json --asc hardware.asc --pcf ./up5k.pcf --pre-pack ./sdc.py
	$(ICEPACK) hardware.asc hardware.bin

.PHONY: upload
upload: hardware.bin
	$(ICEPROG) hardware.bin

.PHONY: clean
clean:
	rm hardware.json hardware.asc hardware.bin