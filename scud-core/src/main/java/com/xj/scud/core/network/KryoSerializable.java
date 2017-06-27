package com.xj.scud.core.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xj.scud.core.exception.SerializableException;
import de.javakaffee.kryoserializers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Author: xiajun
 * Date: 2017/01/03 16:01
 */
public class KryoSerializable<T> implements RpcSerializable<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(KryoSerializable.class);
    private final static RpcSerializable SERIALIZABLE = new KryoSerializable();

    private KryoSerializable() {
    }

    private final static ThreadLocal<Kryo> kryoHolder = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return newKryoInstance();
        }
    };

    public static RpcSerializable newInstance() {
        return SERIALIZABLE;
    }

    private static Kryo newKryoInstance() {
        Kryo kryo = new KryoReflectionFactorySupport();
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(EnumMap.class, new EnumMapSerializer());
        kryo.register(URI.class, new URISerializer());
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        return kryo;
    }

    @Override
    public T decode(byte[] value, Class clazz) {
        try {
            Input in = new Input(value.length);
            in.setBuffer(value);
            return (T) this.kryoHolder.get().readClassAndObject(in);
        } catch (Exception e) {
            SerializableException ex = new SerializableException("Kryo deserialize error", e);
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public byte[] encode(T value) {
        Output output = new Output(128, 10 * 1024 * 1024);
        try {
            this.kryoHolder.get().writeClassAndObject(output, value);
            return output.getBuffer();
        } catch (Exception e) {
            SerializableException ex = new SerializableException("Kryo serialize error", e);
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
