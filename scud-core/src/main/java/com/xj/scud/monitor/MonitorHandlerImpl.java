package com.xj.scud.monitor;

import com.xj.scud.clent.ClientConfig;
import com.xj.scud.clent.ScudClientFactory;
import com.xj.scud.clent.route.RouteEnum;
import com.xj.scud.commons.Config;
import com.xj.scud.core.network.SerializableEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/25 11:31
 */
public class MonitorHandlerImpl implements MonitorHandlerInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorHandlerImpl.class);
    private static MonitorHandlerInterface monitorHandler;

    public MonitorHandlerImpl() {
        if (monitorHandler == null) {
            synchronized (MonitorHandlerImpl.class) {
                if (monitorHandler == null) {
                    ClientConfig conf = new ClientConfig();
                    conf.setRoute(RouteEnum.RANDOM).setTimeout(2000).setInterfaze(MonitorHandlerInterface.class).setVersion("1.0").setWorkThreadSize(1)
                            .setType(SerializableEnum.PROTOBUF).setUseZk(true).setZkHost(Config.getValue("zk.host"));
                    monitorHandler = ScudClientFactory.getServiceConsumer(conf);
                }
            }
        }
    }

    @Override
    public void monitor(String appName, String serviceName, String method, String version, TopPercentile topPercentile) {
        if (monitorHandler != null) {
            try {
                monitorHandler.monitor(appName, serviceName, method, version, topPercentile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.error("Monitor handler exception, create instance fail.");
        }
    }
}
