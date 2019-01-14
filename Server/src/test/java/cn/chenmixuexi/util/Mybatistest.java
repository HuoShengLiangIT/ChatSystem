package cn.chenmixuexi.util;

import cn.chenmixuexi.bean.User;
import cn.chenmixuexi.dao.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class Mybatistest {
    @Test
    public void get(){
        SqlSession session = MyBatisUtils.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectByPrimaryKey(1);
        System.out.println(user);
    }
}
