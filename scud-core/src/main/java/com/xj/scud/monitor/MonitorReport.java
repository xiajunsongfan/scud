package com.xj.scud.monitor;

import com.xj.scud.commons.Config;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/26 09:52
 */
public class MonitorReport {
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorReport.class);
    private final static ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("Scud-monitor-thread", true));
    private volatile static boolean run = false;

    public static void init() {
        if (Config.METHOD_MONITOR && !run) {
            run = true;
            int initDelay = (int) (System.currentTimeMillis() / 1000) % 60;
            es.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Map<String, PerformanceData.TopPercentile> report = PerformanceMonitor.monitor();
                    for (Map.Entry<String, PerformanceData.TopPercentile> entry : report.entrySet()) {
                        PerformanceData.TopPercentile tp = entry.getValue();
                        LOGGER.info(tp.toString());
                    }
                }
            }, initDelay + 60, 60, TimeUnit.SECONDS);
        }
    }
}
