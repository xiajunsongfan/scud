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
     * @param topPercentile tp性能数据
     */
    void monitor(TopPercentile topPercentile);
}
