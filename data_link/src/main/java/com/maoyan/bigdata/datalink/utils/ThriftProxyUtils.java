package com.maoyan.bigdata.datalink.utils;

import com.meituan.service.mobile.mtthrift.proxy.ThriftClientProxy;

public class ThriftProxyUtils {

    public static ThriftClientProxy getProxy(String remoteAppkey, Class serverInterfaceClazz, String ip,int defaultPort) throws Exception {
        ThriftClientProxy proxy = new ThriftClientProxy();
        proxy.setRemoteAppkey(remoteAppkey);
        proxy.setAppKey("com.sankuai.movie.bigdata.datalink");
        proxy.setServiceInterface(serverInterfaceClazz);
        proxy.setTimeout(20000);
        if (ip != null && !"".equalsIgnoreCase(ip)) {
            proxy.setServerIpPorts(ip);
        }
        if(defaultPort>0){
            proxy.setRemoteServerPort(defaultPort);
        }
        proxy.afterPropertiesSet();
        return proxy;
    }
}
