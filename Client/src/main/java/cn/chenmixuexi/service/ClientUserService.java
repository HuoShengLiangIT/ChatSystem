package cn.chenmixuexi.service;


import io.netty.channel.Channel;

import java.io.IOException;

/**
 * 客户端用户服务接口
 */
public interface ClientUserService {
    /**
     *
     * @param channel
     * @param name
     * @param password
     * @return
     */
    boolean Login(Channel channel, String name, String password);

    boolean Register(Channel channel,String name,String password,String email);

    boolean ForgetPassWord(Channel channel, String name, String email);

    boolean ModifyPassWord(Channel channel, String oldPassword, String newPassword);

    boolean GetAllUsers(Channel channel) throws IOException;

    boolean Quit(Channel channel);

    boolean SendUserMsg(Channel channel, String toname, String msg);

    boolean SendAllUserMsg(Channel channel, String msg);

    boolean SendUserFile(Channel channel, String name, String path);
}
