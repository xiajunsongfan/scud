package com.xj.scud.core.network;

/**
 * Author: xiajun
 * Date: 2017/01/03 12:07
 */
public enum SerializableEnum {
    PROTOBUF((byte) 0x01, "protobuf"), KRYO((byte) 0x02, "kryo");
    byte value;
    String name;

    SerializableEnum(byte v, String name) {
        this.value = v;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
