
APIO_DIR=~/.apio
YOSYS=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/yosys
NEXTPNR=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/nextpnr-ice40
ICEPACK=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/icepack
ICEPROG=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/iceprog


.PHONY: clean
clean:
	rm hardware.json hardware.asc hardware.bin

.PHONY: up5k
up5k: BlackBoxSPRAM.v Top.v
	$(YOSYS) -p "synth_ice40 -json hardware.json" -q BlackBoxSPRAM.v Top.v
	$(NEXTPNR) --up5k --package sg48 --json hardware.json --asc hardware.asc --pcf ./pinout/up5k.pcf --pre-pack ./timing/sdc.py
	$(ICEPACK) hardware.asc hardware.bin

.PHONY: upload
upload: hardware.bin
	$(ICEPROG) hardware.bin
