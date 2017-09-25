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
            int initDelay = (int) (System.currentTimeMillis() / 1000) % 10;
            es.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Map<String, TopPercentile> report = PerformanceMonitor.monitor();
                    for (Map.Entry<String, TopPercentile> entry : report.entrySet()) {
                        TopPercentile tp = entry.getValue();
                        if (mdi == null) {
                            LOGGER.info(tp.toString());
                        } else {
                            String[] smv = entry.getKey().split(":");
                            System.out.println("------------------------------------->");
                            mdi.monitor(Config.APP_NAME, smv[0], smv[1], smv[2], tp);
                            LOGGER.info(tp.toString());
                        }
                    }
                }
            }, initDelay + 10, 10, TimeUnit.SECONDS);
        }
    }
}
