package com.xj.scud.console.monitor;

import com.xj.scud.annotation.Scud;
import com.xj.scud.console.view.MonitorChartBean;
import com.xj.scud.monitor.MonitorHandlerInterface;
import com.xj.scud.monitor.TopPercentile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/25 11:58
 */
@Scud(version = "1.0")
public class ConsoleMonitorHandlerImpl implements MonitorHandlerInterface {
    private final static Map<String, Map<String, Map<String, Map<String, LinkedList<TopPercentile>>>>> appMap = new ConcurrentHashMap<>();

    @Override
    public void monitor(TopPercentile tp) {
        System.out.println(tp.toString());
        Map<String, Map<String, Map<String, LinkedList<TopPercentile>>>> serviceMap = appMap.get(tp.getAppName());
        if (serviceMap == null) {
            serviceMap = new ConcurrentHashMap<>();
            appMap.put(tp.getAppName(), serviceMap);
        }
        Map<String, Map<String, LinkedList<TopPercentile>>> methodMap = serviceMap.get(tp.getServiceName());
        if (methodMap == null) {
            methodMap = new ConcurrentHashMap<>();
            serviceMap.put(tp.getServiceName(), methodMap);
        }
        Map<String, LinkedList<TopPercentile>> versionMap = methodMap.get(tp.getMethod());
        if (versionMap == null) {
            versionMap = new ConcurrentHashMap<>();
            methodMap.put(tp.getMethod(), versionMap);
        }
        LinkedList<TopPercentile> tpList = versionMap.get(tp.getVersion());
        if (tpList == null) {
            tpList = new LinkedList<>();
            versionMap.put(tp.getVersion(), tpList);
        }
        tpList.addLast(tp);
        if (tpList.size() >= 15) {
            tpList.removeFirst();
        }
    }

    public static Collection<String> getAppList() {
        return appMap.keySet();
    }

    public static Collection<String> getServiceList(String appName) {
        return appMap.get(appName).keySet();
    }

    public static Collection<TopPercentile> getMethodList(String appName) {
        Map<String, TopPercentile> map = new HashMap<>();
        Map<String, Map<String, Map<String, LinkedList<TopPercentile>>>> serviceMap = appMap.get(appName);
        if (serviceMap != null) {
            for (Map.Entry<String, Map<String, Map<String, LinkedList<TopPercentile>>>> entry : serviceMap.entrySet()) {
                for (Map.Entry<String, Map<String, LinkedList<TopPercentile>>> methodMap : entry.getValue().entrySet()) {
                    for (Map.Entry<String, LinkedList<TopPercentile>> versionMap : methodMap.getValue().entrySet()) {
                        for (TopPercentile tp : versionMap.getValue()) {
                            String key = tp.getServiceName() + ":" + tp.getMethod() + ":" + tp.getVersion();
                            map.put(key, tp);
                        }
                    }
                }
            }
        }
        return map.values();
    }

    public static MonitorChartBean getMonitorData(String appName, String service, String method, String version) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        MonitorChartBean chartBean = new MonitorChartBean();
        Map<String, Map<String, Map<String, LinkedList<TopPercentile>>>> apps = appMap.get(appName);
        if (apps != null) {
            Map<String, Map<String, LinkedList<TopPercentile>>> serviceMap = apps.get(service);
            if (serviceMap != null) {
                Map<String, LinkedList<TopPercentile>> methodMap = serviceMap.get(method);
                if (methodMap != null) {
                    LinkedList<TopPercentile> versions = methodMap.get(version);
                    for (TopPercentile tp : versions) {
                        String key = tp.getServiceName() + ":" + tp.getMethod() + ":" + tp.getVersion();
                        chartBean.addTP50(tp.getTp50());
                        chartBean.addTP90(tp.getTp90());
                        chartBean.addTP99(tp.getTp99());
                        chartBean.addTP999(tp.getTp999());
                        chartBean.addMAX(tp.getMax());
                        chartBean.setTitle(key);
                        chartBean.addTime(format.format(new Date(tp.getTime() * 1000L)));
                    }
                }
            }
        }
        return chartBean;
    }

    public static MonitorChartBean getMonitorLastData(String appName, String service, String method, String version) {
        Map<String, Map<String, Map<String, LinkedList<TopPercentile>>>> apps = appMap.get(appName);
        if (apps != null) {
            Map<String, Map<String, LinkedList<TopPercentile>>> serviceMap = apps.get(service);
            if (serviceMap != null) {
                Map<String, LinkedList<TopPercentile>> methodMap = serviceMap.get(method);
                if (methodMap != null) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    LinkedList<TopPercentile> versions = methodMap.get(version);
                    TopPercentile tp = versions.getLast();
                    MonitorChartBean chartBean = new MonitorChartBean();
                    chartBean.addTime(format.format(new Date(tp.getTime() * 1000L)));
                    chartBean.addTP50(tp.getTp50());
                    chartBean.addTP90(tp.getTp90());
                    chartBean.addTP99(tp.getTp99());
                    chartBean.addTP999(tp.getTp999());
                    chartBean.addMAX(tp.getMax());
                    return chartBean;
                }
            }
        }
        return null;
    }
}
