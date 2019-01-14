package cn.chenmixuexi.service.impl;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.bean.User;
import cn.chenmixuexi.dao.OfflineMsgMapper;
import cn.chenmixuexi.dao.UserMapper;
import cn.chenmixuexi.service.UserService;
import cn.chenmixuexi.util.EmailUtils;
import cn.chenmixuexi.util.JedisUtils;
import cn.chenmixuexi.util.MyBatisUtils;
import io.netty.channel.Channel;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Set;

public class UserServiceImpl implements UserService {
    UserMapper mapper = MyBatisUtils.getSession().getMapper(UserMapper.class);
    OfflineMsgMapper offlineMsgMapper = MyBatisUtils.getSession().getMapper(OfflineMsgMapper.class);
    private static Jedis jedis = JedisUtils.getJedis();

    @Override
    public boolean Login(String name, String password) {
        jedis.sadd("userlist",name);
        User user = mapper.selectByName(name);

        if(user!=null&&user.getPwd().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public boolean Register(String name, String password, String email) {
        User user = new User();
        user.setName(name);
        user.setPwd(password);
        user.setEmail(email);
        int insert = mapper.insert(user);
        return insert>=1;
    }

    @Override
    public boolean ForgetPassWord(String name, String email) {
        User user = mapper.selectByName(name);
        if(user!=null&&user.getEmail().equals(email)){
            EmailUtils.sendEmail(email,"用户名:"+user.getName()+"   "+"你的密码为："+user.getPwd());
            return true;
        }
        return false;
    }

    @Override
    public boolean ModifyPassWord(String name, String oldPassword, String newPassword) {
        User user = mapper.selectByName(name);
        if(user!=null&&user.getPwd().equals(oldPassword)){
            user.setPwd(newPassword);
            int i = mapper.updateByPrimaryKey(user);
            return i>0;
        }
        return false;
    }

    @Override
    public User getUser(String toname) {
        return mapper.selectByName(toname);
    }

    @Override
    public Set<String> getAllUsers() {
        return jedis.smembers("userlist");
    }

    @Override
    public boolean OffLine(String name) {
        return jedis.srem("userlist", name)>0;
    }

    @Override
    public Boolean isUserOnline(String toname) {
        return jedis.sismember("userlist", toname);
    }

    @Override
    public boolean addOffLineMsg(OfflineMsg offlineMsg) {
        return offlineMsgMapper.insertSelective(offlineMsg)>0;
    }


}
