package com.xj.scud.monitor;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/25 10:51
 * 监控的性能数据处理接口
 */
public interface MonitorHandlerInterface {
    /**
     * 监控数据
     *
     * @param appName       运用名称
     * @param method        接口方法名称
     * @param topPercentile tp性能数据
     */
    void monitor(String appName, String serviceName, String method, String version, TopPercentile topPercentile);
}
