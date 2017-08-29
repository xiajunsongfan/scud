package com.xj.scud;

import com.xj.scud.core.RpcResult;
import com.xj.scud.core.network.ProtobufSerializable;
import com.xj.scud.core.network.RpcSerializable;
import com.xj.scud.monitor.PerformanceData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Author: baichuan - xiajun
 * Date: 2017/01/09 14:53
 */
@State(Scope.Benchmark)
public class JMHSampleTest {
    public RpcSerializable serializable = ProtobufSerializable.newInstance();
    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    public String[] data = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public Map<String, String> list = new ConcurrentHashMap<>();
    public PerformanceData pdata = new PerformanceData("test");
    Random random = new Random();

    public JMHSampleTest() {
        list.put("12", "1");
        list.put("32", "2");
        list.put("35", "3");
        list.put("sfs", "4");
        list.put("346s", "5");
        for (int i = 0; i < 180000; i++) {
            pdata.add(random.nextInt(10000));
        }
        System.out.println("===================");
    }

    //@Benchmark
    public void serializable() {
        //RpcSerializable serializable = ProtobufSerializable.newInstance();
        RpcResult result = new RpcResult();
        result.setValue("1234543657568679");
        result.setStatus(200);
        serializable.encode(result);
    }

    @Benchmark
    public void lock() {
        /*pdata.add(random.nextInt(30000));
        if (pdata.getSize() > 180000) {
            pdata = new PerformanceData("test");
        }*/
        pdata.getTP();
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSampleTest.class.getSimpleName())
                .forks(1).warmupIterations(10)
                .threads(2)
                .build();

        new Runner(opt).run();
    }
}
