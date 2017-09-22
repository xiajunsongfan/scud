package com.xj.scud.console;

import com.google.gson.Gson;
import com.xj.scud.commons.Config;
import com.xj.scud.console.util.ConfigUtil;
import com.xj.scud.console.view.AppBean;
import com.xj.scud.console.view.ServerInfoBean;
import com.xj.scud.console.view.ServiceInterfaceBean;
import com.xj.scud.core.ServerNodeStatus;
import com.xj.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:05
 */
@Controller
public class ScudController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScudController.class);
    private final static ZkClient ZK_CLIENT;

    static {
        ZK_CLIENT = new ZkClient(ConfigUtil.getValue("zk.host"), 4000, 5000);
    }

    @RequestMapping(value = "/index.htm")
    public ModelAndView index() {
        ModelAndView mad = new ModelAndView("index");
        List<String> apps = ZK_CLIENT.getChild("/jdns", false);
        List<AppBean> appBeanList = new ArrayList<>();
        if (apps != null) {
            appBeanList = new ArrayList<>(apps.size());
            for (String app : apps) {
                AppBean appBean = new AppBean();
                appBean.setName(app);
                List<String> ifs = ZK_CLIENT.getChild("/jdns/" + app, false);
                List<ServiceInterfaceBean> interfaces = new ArrayList<>();
                if (ifs != null) {
                    interfaces = new ArrayList<>(ifs.size());
                    for (String anIf : ifs) {
                        ServiceInterfaceBean sib = new ServiceInterfaceBean();
                        sib.setName(anIf);
                        interfaces.add(sib);
                    }
                }
                appBean.setInterfaces(interfaces);
                appBeanList.add(appBean);
            }
        }
        mad.addObject("apps", appBeanList);
        return mad;
    }

    @RequestMapping(value = "/serviceDetail.htm")
    public ModelAndView detail(String appName) {
        ModelAndView mad = new ModelAndView("serviceDetail");
        List<String> ifs = ZK_CLIENT.getChild("/jdns/" + appName, false);
        List<ServiceInterfaceBean> interfaces = new ArrayList<>();
        if (ifs != null) {
            interfaces = new ArrayList<>(ifs.size());
            for (String anIf : ifs) {

                List<String> versions = ZK_CLIENT.getChild("/jdns/" + appName + "/" + anIf, false);
                if (versions != null) {
                    for (String version : versions) {
                        ServiceInterfaceBean sib = new ServiceInterfaceBean();
                        sib.setName(anIf);
                        sib.setVersion(version);
                        List<String> hosts = ZK_CLIENT.getChild("/jdns/" + appName + "/" + anIf + "/" + version, false);
                        List<ServerInfoBean> infos = new ArrayList<>();
                        if (hosts != null) {
                            infos = new ArrayList<>(hosts.size());
                            for (String host : hosts) {
                                ServerInfoBean bean = new ServerInfoBean();
                                String[] ipPort = host.split(":");
                                bean.setIp(ipPort[0]);
                                bean.setPort(ipPort[1]);
                                byte[] data = ZK_CLIENT.getData("/jdns/" + appName + "/" + anIf + "/" + version + "/" + host, false);
                                if (data != null && data.length > 1) {
                                    Gson gson = new Gson();
                                    ServerNodeStatus sns = gson.fromJson(new String(data), ServerNodeStatus.class);
                                    bean.setStatus(sns.getStatus());
                                }
                                infos.add(bean);
                            }
                        }
                        sib.setServers(infos);
                        interfaces.add(sib);
                    }
                }
            }
        }
        mad.addObject("appName", appName);
        mad.addObject("interfaces", interfaces);
        return mad;
    }

    @RequestMapping(value = "/offline.htm")
    public ModelAndView offline(String path, String appName) {
        ServerNodeStatus status = new ServerNodeStatus(-1);
        Gson gson = new Gson();
        String data = gson.toJson(status);
        ZK_CLIENT.setData(Config.DNS_PREFIX + appName + "/" + path, data.getBytes(Charset.forName("UTF-8")));
        return this.detail(appName);
    }

    @RequestMapping(value = "/online.htm")
    public ModelAndView online(String path, String appName) {
        ServerNodeStatus status = new ServerNodeStatus(1);
        Gson gson = new Gson();
        String data = gson.toJson(status);
        ZK_CLIENT.setData(Config.DNS_PREFIX + appName + "/" + path, data.getBytes(Charset.forName("UTF-8")));
        return this.detail(appName);
    }
}
