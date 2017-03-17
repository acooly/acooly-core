/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-01-26 13:01 创建
 *
 */
package com.acooly.core.utils.kryos;

import java.util.Arrays;

import com.acooly.core.utils.ShutdownHooks;
import com.esotericsoftware.kryo.serializers.EnumNameSerializer;
import org.springframework.util.Assert;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

/**
 * 提供线程安全的Kryo对象和可重用的Output,增强性能,减少GC.
 * @author qzhanbo@yiji.com
 */
public class Kryos {

    /**
     * 输出缓冲区最大大小Integer.MAX_VALUE
     */
    public static final int MAX_BUFFER_SIZE = -1;

    /**
     * 输出缓冲区初始大小
     */
    public static final int INITIAL_BUFFER_SIZE = 1024 * 2;

    /**
     * 由于Kryo对象不是threadsafe的，所以在线程内缓存
     */
    private static ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new ReflectionFactorySupportKryo();
            kryo.setDefaultSerializer(DuplicateFieldNameAcceptedCompatibleFieldSerializer.class);
            //注册kryo不能序列化的类
            kryo.register(Arrays.asList("a").getClass(), new JavaSerializer());
            kryo.addDefaultSerializer(Enum.class, EnumNameSerializer.class);
            return kryo;
        }
    };
    /**
     * 尽量重用Output,减小数组对象分配,减轻gc压力.
     */
    private static ThreadLocal<Output> outputThreadLocal = new ThreadLocal<Output>() {
        protected Output initialValue() {
            return new Output(Kryos.INITIAL_BUFFER_SIZE, Kryos.MAX_BUFFER_SIZE);
        }
    };

    static {
        ShutdownHooks.addShutdownHook(new Runnable() {
            @Override
            public void run() {
                if (kryoThreadLocal != null) {
                    kryoThreadLocal.remove();
                }
                if (outputThreadLocal != null) {
                    outputThreadLocal.remove();
                }
            }
        }, "KryosShutdownHook");
    }

    /**
     * 获取线程安全的Kryo对象
     * @return Kryo
     */
    public static Kryo getKryo() {
        return kryoThreadLocal.get();
    }

    /**
     * 获取线程安全的Output,尽量重用Output,减小数组对象分配,减轻gc压力.
     * <p>
     * 注意:在使用完后,请调用{@link com.esotericsoftware.kryo.io.Output#clear()}
     * 复原内部数组,供下次使用
     * </p>
     * <p>
     * 性能测试: 每次new Output<br/>
     *
     * <pre>
     *   {@code
     *
     *   KryoTest.testKryo: [measured 2000000 out of 2000100 rounds, threads: 4 (all cores)]
     *    round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 88, GC.time: 6.98, time.total: 15.71, time.warmup: 0.89, time.bench: 14.81
     *
     *   KryoTest.testKryo: [measured 2000000 out of 2000100 rounds, threads: 4 (all cores)]
     *    round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 89, GC.time: 7.36, time.total: 17.71, time.warmup: 0.87, time.bench: 16.84
     *
     *  }
     *  </pre>
     *
     * 使用此方法<br/>
     *
     * <pre>
     *   {@code
     *
     *   KryoTest.testKryo: [measured 2000000 out of 2000100 rounds, threads: 4 (all cores)]
     *   round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 62, GC.time: 6.15, time.total: 14.98, time.warmup: 0.90, time.bench: 14.07
     *
     *   KryoTest.testKryo: [measured 2000000 out of 2000100 rounds, threads: 4 (all cores)]
     *   round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00], GC.calls: 64, GC.time: 6.63, time.total: 16.99, time.warmup: 0.89, time.bench: 16.10
     *    }
     *  </pre>
     * 可以看到上面的测试,GC次数减少四分之一.结合我们的使用场景,我们存储的对象都比较大,占用的内存比较大,实际场景GC次数减少次数更优!
     * </p>
     *
     * @return Output
     */
    public static Output getOutput() {
        return outputThreadLocal.get();
    }

    /**
     * 把对象序列化为二进制,不写入类型信息
     */
    public static <T> byte[] writeObject(T object) {
        Kryo kryo = getKryo();
        Output output = getOutput();
        try {
            kryo.writeObject(output, object);
            return output.toBytes();
        } finally {
            output.clear();
        }
    }

    /**
     * 把二进制代码反序列化为对象，不读取类型信息
     *
     */
    public static <T> T readObject(byte[] in, Class<T> toClass) {
        Assert.notNull(toClass, "类型不能为null");
        if (in == null) {
            return null;
        }
        Kryo kryo = getKryo();
        Input input = new Input();
        input.setBuffer(in);
        T result = kryo.readObject(input, toClass);
        return result;
    }

    /**
     * 把对象序列化为二进制,会写入类型信息
     */
    public static byte[] serialize(Object o) {
        Kryo kryo = getKryo();
        Output output = getOutput();
        try {
            kryo.writeClassAndObject(output, o);
            return output.toBytes();
        } finally {
            output.clear();
        }

    }

    /**
     * 把二进制代码反序列化为对象，会读取类型信息
     *
     */
    public static <T> T deserialize(byte[] in, Class<T> toClass) {
        Assert.notNull(toClass, "类型不能为null");
        if (in == null) {
            return null;
        }
        Kryo kryo = getKryo();
        Input input = new Input();
        input.setBuffer(in);
        Object result = kryo.readClassAndObject(input);
        return toClass.cast(result);
    }

    /**
     * 把二进制代码反序列化为对象，会读取类型信息
     *
     */
    public static Object deserialize(byte[] in) {
        if (in == null) {
            return null;
        }
        Kryo kryo = getKryo();
        Input input = new Input();
        input.setBuffer(in);
        return kryo.readClassAndObject(input);
    }
}
