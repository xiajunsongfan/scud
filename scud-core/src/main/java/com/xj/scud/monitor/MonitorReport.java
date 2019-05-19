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
 * Author: xiajun
 * Date: 2017/08/26 09:52
 */
public class MonitorReport {
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorReport.class);
    private final static ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("Scud-monitor-thread", true));
    private volatile static boolean run = false;
    private static MonitorHandlerInterface mdi;

    public static void init(String handler) {
        if (Config.METHOD_MONITOR && !run) {
            run = true;
            if (handler != null && !"".equals(handler)) {
                try {
                    mdi = (MonitorHandlerInterface) Class.forName(handler).newInstance();
                } catch (Exception e) {
                    LOGGER.error("Monitor handler new instance fail.", e);
                }
            }
            int initDelay = (int) (System.currentTimeMillis() / 1000) % 60;
            es.scheduleAtFixedRate(() -> {
                Map<String, TopPercentile> report = PerformanceMonitor.monitor();
                for (Map.Entry<String, TopPercentile> entry : report.entrySet()) {
                    TopPercentile tp = entry.getValue();
                    String[] smv = entry.getKey().split(":");
                    tp.setAppName(Config.APP_NAME);
                    tp.setServiceName(smv[0]);
                    tp.setMethod(smv[1]);
                    tp.setVersion(smv[2]);
                    tp.setTime((int) (System.currentTimeMillis() / 1000));
                    if (mdi == null) {
                        LOGGER.info(tp.toString());
                    } else {
                        mdi.monitor(tp);
                        LOGGER.info(tp.toString());
                    }
                }
            }, initDelay + 60, 60, TimeUnit.SECONDS);
        }
    }
}
