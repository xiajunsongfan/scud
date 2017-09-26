package com.xj.scud.console;

import com.google.gson.Gson;
import com.xj.scud.commons.Config;
import com.xj.scud.console.monitor.ConsoleMonitorHandlerImpl;
import com.xj.scud.console.util.ConfigUtil;
import com.xj.scud.console.view.AppBean;
import com.xj.scud.console.view.MonitorChartBean;
import com.xj.scud.console.view.ServerInfoBean;
import com.xj.scud.console.view.ServiceInterfaceBean;
import com.xj.scud.core.ServerNodeStatus;
import com.xj.scud.monitor.TopPercentile;
import com.xj.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:05
 */
@Controller
public class ScudController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScudController.class);
    private final static ZkClient ZK_CLIENT;
    private static Map<String, AppBean> APP_MAP;

    static {
        ZK_CLIENT = new ZkClient(ConfigUtil.getValue("zk.host"), 4000, 5000);
    }

    @RequestMapping(value = "/index.htm")
    public ModelAndView index() {
        ModelAndView mad = new ModelAndView("index");
        List<String> interfaceList = ZK_CLIENT.getChild("/jdns", false);
        Map<String, AppBean> appMap = new HashMap<>(32);
        if (interfaceList != null) {
            for (String interfacz : interfaceList) {
                String appName = new String(ZK_CLIENT.getData(Config.DNS_PREFIX + interfacz), Charset.forName("UTF-8"));
                AppBean appBean = appMap.get(appName);
                if (appBean == null) {
                    appBean = new AppBean();
                    appMap.put(appName, appBean);
                    appBean.setName(appName);
                }
                List<ServiceInterfaceBean> interfaces = appBean.getInterfaces();
                if (interfaces == null) {
                    interfaces = new ArrayList<>(8);
                    appBean.setInterfaces(interfaces);
                }
                ServiceInterfaceBean sifb = new ServiceInterfaceBean();
                sifb.setName(interfacz);
                interfaces.add(sifb);
            }
        }
        APP_MAP = appMap;
        mad.addObject("apps", appMap.values());
        return mad;
    }

    @RequestMapping(value = "/serviceDetail.htm")
    public ModelAndView detail(String appName) {
        ModelAndView mad = new ModelAndView("serviceDetail");
        AppBean appBean = APP_MAP.get(appName);
        List<ServiceInterfaceBean> interfaces = new ArrayList<>();
        if (appBean != null) {
            List<ServiceInterfaceBean> sifb = appBean.getInterfaces();
            for (ServiceInterfaceBean anIf : sifb) {
                List<String> versions = ZK_CLIENT.getChild(Config.DNS_PREFIX + anIf.getName(), false);
                if (versions != null) {
                    for (String version : versions) {
                        ServiceInterfaceBean sib = new ServiceInterfaceBean();
                        sib.setName(anIf.getName());
                        sib.setVersion(version);
                        List<String> hosts = ZK_CLIENT.getChild(Config.DNS_PREFIX + anIf.getName() + "/" + version, false);
                        List<ServerInfoBean> infos = new ArrayList<>();
                        if (hosts != null) {
                            infos = new ArrayList<>(hosts.size());
                            for (String host : hosts) {
                                ServerInfoBean bean = new ServerInfoBean();
                                String[] ipPort = host.split(":");
                                bean.setIp(ipPort[0]);
                                bean.setPort(ipPort[1]);
                                byte[] data = ZK_CLIENT.getData(Config.DNS_PREFIX + anIf.getName() + "/" + version + "/" + host, false);
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

    @RequestMapping(value = "/consoleList.htm")
    public ModelAndView consoleList() {
        ModelAndView mad = new ModelAndView("consoleList");
        mad.addObject("apps", ConsoleMonitorHandlerImpl.getAppList());
        return mad;
    }

    @RequestMapping(value = "/consoleMethodList.htm")
    public ModelAndView consoleMethodList(String appName) {
        ModelAndView mad = new ModelAndView("consoleMethodList");
        mad.addObject("methods", ConsoleMonitorHandlerImpl.getMethodList(appName));
        mad.addObject("appName", appName);
        return mad;
    }

    @RequestMapping(value = "/consoleDetail.htm")
    public ModelAndView consoleDetail(String appName, String serviceName, String methodName, String version) {
        ModelAndView mad = new ModelAndView("consoleDetail");
        mad.addObject("data", ConsoleMonitorHandlerImpl.getMonitorData(appName, serviceName, methodName, version));
        mad.addObject("appName", appName);
        return mad;
    }

    @RequestMapping(value = "/getMonitorData.htm")
    @ResponseBody
    public String getMonitorData(String appName, String key, String time) {
        String[] args = key.split(":");
        MonitorChartBean chartBean = ConsoleMonitorHandlerImpl.getMonitorLastData(appName, args[0], args[1], args[2]);
        if (chartBean != null) {
            if (!time.equals(chartBean.getTime().get(0))) {
                Gson gson = new Gson();
                return gson.toJson(chartBean);
            }
        }
        return "{}";
    }
}
