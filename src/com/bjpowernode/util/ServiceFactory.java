package com.bjpowernode.util;



/**
 * 作者:fyc
 * 2019/8/27
 */
public class ServiceFactory {
    public static Object getService(Object service){
        return new TransactionInvocationHandler(service).getProxy();
    }
}
