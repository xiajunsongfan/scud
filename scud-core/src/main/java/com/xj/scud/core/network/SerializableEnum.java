package com.xj.scud.core.network;

/**
 * Author: xiajun
 * Date: 2017/01/03 12:07
 */
public enum SerializableEnum {
    PROTOBUF((byte) 0x01), KRYO((byte) 0x02);
    byte value;

    SerializableEnum(byte v) {
        this.value = v;
    }

    public byte getValue() {
        return value;
    }
}
