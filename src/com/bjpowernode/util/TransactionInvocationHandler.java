package com.bjpowernode.util;

import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 作者:fyc
 * 2019/8/27
 * 代理类
 */
public class TransactionInvocationHandler implements InvocationHandler {
    //目标类
    private  Object target;

    public TransactionInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession session =null;
        Object obj = null;
        try{
            //取得连接，开启事务
            session = SqlSessionUtil.getSession();
            //目标类的方法
            obj = method.invoke(target, args);
            //提交事务
            session.commit();
        }catch (Exception e){
            //回滚事务
            session.rollback();
            e.printStackTrace();
        }finally {
            //关闭通道
            SqlSessionUtil.myClose(session);
        }
        return obj;
    }
    public   Object getProxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}
