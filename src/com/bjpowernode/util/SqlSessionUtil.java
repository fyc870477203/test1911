package com.bjpowernode.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者:fyc
 * 2019/8/27
 */
public class SqlSessionUtil {
    private SqlSessionUtil(){}//禁止new SqlSessionUtil对象调用方法 只能通过类名.方法名来调用
    private static SqlSessionFactory sqlSessionFactory;
    //定义一个私有的静态的

    static{
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        }catch (IOException e){
            e.printStackTrace();
        }
        sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
    }
    private static ThreadLocal<SqlSession> t = new ThreadLocal<>();//创建一个基于当前线程的本地仓库 泛型是Sqlsession连接通道

    public static SqlSession getSession(){//调用这个方法时
        SqlSession session = t.get();//现在ThreadLocal没有值 为null 肯定取不出session
        if(session ==  null){//如果session为null ，便创建一个session 并添加到基于当前线程的本地仓库
            session = sqlSessionFactory.openSession();
            t.set(session);
        }
        return session;//返回一个session值
    }
    //关闭sqlSession通道
    public static void myClose(SqlSession session){
        if(session != null){//如果session不为null 当调用这个方法时就关闭session通道
                            //并将session从通道中删除
            session.close();
            //将sqlSession从t中移除掉
            t.remove();
        }
    }
}
