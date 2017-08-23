package com.xj.scud.spring.bean;

import com.xj.scud.server.Provider;
import com.xj.scud.server.ScudServer;
import com.xj.scud.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 15:13
 */
public class ServerBean implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerBean.class);
    private List<ProviderBean> providers;

    public List<ProviderBean> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderBean> providers) {
        this.providers = providers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (providers == null || providers.isEmpty()) {
            throw new IllegalArgumentException("provider can't null.");
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Provider[] provs = new Provider[providers.size()];
        for (int i = 0; i < providers.size(); i++) {
            ProviderBean providerBean = providers.get(i);
            provs[i] = new Provider(providerBean.getInterfaze(), providerBean.getRef(), providerBean.getVersion());
        }
        ScudServer server = new ScudServer(provs);
        server.start();
    }
}
