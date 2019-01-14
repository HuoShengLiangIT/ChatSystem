package cn.chenmixuexi.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory = null;
    static {
        String configFile = "mybatis.xml";
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(MyBatisUtils.class.getClassLoader().getResourceAsStream(configFile));
    }
    private MyBatisUtils(){}

    public static SqlSession getSession(){
        //默认不会自动提交事务
        return sqlSessionFactory.openSession(true);
    }
}
