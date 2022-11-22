APIO_DIR=~/.apio
YOSYS=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/yosys
NEXTPNR=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/nextpnr-ice40
ICEPACK=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/icepack
ICEPROG=$(APIO_DIR)/packages/tools-oss-cad-suite/bin/iceprog
ICETIME=icetime
SRC=..

.PHONY: init
init:
	apio init --board icestick

.PHONY: build
build: $(SRC)/init.bin $(SRC)/bin.hack $(SRC)/Top.v
	cp $(SRC)/init.bin ./ 
	cp $(SRC)/bin.hack ./ 
	cp $(SRC)/Top.v ./
	$(YOSYS) -p "synth_ice40 -json hardware.json" -q $(SRC)/Top.v
	$(NEXTPNR) --hx1k --package vq100 --json hardware.json --asc hardware.asc --pcf icestick.pcf --pre-pack ./sdc.py
	$(ICEPACK) hardware.asc hardware.bin

.PHONY: upload
upload: hardware.bin
	apio upload

.PHONY: time
time: 
	$(NEXTPNR) --hx1k --package vq100 --json hardware.json --asc hardware.asc --pcf icestick.pcf  --pre-pack ./sdc.py
	$(ICETIME) -d hx1k -P vq100 -C "/home/kaduv/.apio/packages/tools-oss-cad-suite/share/icebox/chipdb-1k.txt" -mtr hardware.rpt hardware.asc


.PHONY: clean
clean:
	rm Top.v init.bin bin.hack
	rm hardware.json hardware.asc hardware.bin *.dblite