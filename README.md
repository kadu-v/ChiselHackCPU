# Chisel HackCPU

## ハードウェアのセットアップ
1. 論理合成ツールのインストール

    ```
    $ pip3 install -U apio
    $ apio install -all
    ```
    [apio](https://github.com/FPGAwars/apio#documentation)

2. プログラマーのセットアップ   
    1. Arduino IDEのインストールが必要
        [ここ](https://www.arduino.cc/en/software)からインストール  
        Ubuntu 22.04では、AppImageは`libfuse2`ライブラリが必要
        ```
        $ sudo apt install -y libfuse2
        $ ./arduino-ide_2.0.0_Linux_64bit.AppImage
        ```
    2. プログラマーのファームウェアをダウンロード  
        [iceprog.ino](https://github.com/OLIMEX/iCE40HX1K-EVB/blob/master/programmer/olimexino-32u4%20firmware/iceprog.ino)
        ```
        $ git clone https://github.com/OLIMEX/iCE40HX1K-EVB.git
        ```
    3. USBをolimexino-32u4に刺して、ポートを選択  
        '/dev/ttyACM0 (Arduino Leonardo)' または '/dev/ttyUSB0 (Arduino Leonardo)'  
    4. SPIFlash ライブラリをインストール
        [SPIFlash v2.2.0](https://github.com/Marzogh/SPIMemory/releases/tag/v2.2.0)からzipをダウンロード.  
        `Load the library in Arduino from Sketch` -> `Include Library` -> `Add .ZIP library`...

    5. iceprogduinoのビルドとインストール  
        2.の[iceprogduino](https://github.com/OLIMEX/iCE40HX1K-EVB/tree/master/programmer/iceprogduino)を以下でビルド&install
        ```
        $ make 
        $ sudo make install
          cp iceprogduino /usr/local/bin/iceprogduino...
        ```
        [alimex wiki](https://www.olimex.com/wiki/ICE40HX1K-EVB#Preparing_OLIMEXINO-32U4_as_programmer)

## Lチカで動作確認
    ```
    $ apio examples -d iCE40-HX1K-EVB/leds
    $ apio upload
    ```

## ピンアウトのドキュメント
この[リポジトリ](https://github.com/OLIMEX/iCE40HX1K-EVB/blob/master)の[iCE40HX1K-EVB_Rev_B.pdf](https://github.com/OLIMEX/iCE40HX1K-EVB/blob/master/iCE40HX1K-EVB_Rev_B.pdf)に記載されている。