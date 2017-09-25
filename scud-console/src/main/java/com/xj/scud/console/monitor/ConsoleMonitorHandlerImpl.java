package com.xj.scud.console.monitor;

import com.xj.scud.annotation.Scud;
import com.xj.scud.monitor.MonitorHandlerInterface;
import com.xj.scud.monitor.TopPercentile;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/25 11:58
 */
@Scud(version = "1.0")
public class ConsoleMonitorHandlerImpl implements MonitorHandlerInterface {
    @Override
    public void monitor(String appName, String serviceName, String method, String version, TopPercentile topPercentile) {
        System.out.println(appName + "  " + method + " " + topPercentile.toString());
    }
}
