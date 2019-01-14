package cn.chenmixuexi.service;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.bean.User;

import java.util.Set;

public interface UserService {
    boolean Login(String name,String password);

    boolean Register(String name,String password,String email);

    boolean ForgetPassWord(String name, String email);

    boolean ModifyPassWord(String name, String oldPassword, String newPassword);

    User getUser(String toname);

    Set<String> getAllUsers();

    boolean OffLine(String name);

    Boolean isUserOnline(String toname);


    boolean addOffLineMsg(OfflineMsg offlineMsg);
}
