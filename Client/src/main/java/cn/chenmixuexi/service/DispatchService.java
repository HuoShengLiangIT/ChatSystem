package cn.chenmixuexi.service;

import cn.chenmixuexi.bean.Config;
import cn.chenmixuexi.controller.NettyClient;
import cn.chenmixuexi.service.impl.ClientUserServiceImpl;
import io.netty.channel.ChannelFuture;

import java.io.IOException;
import java.util.Scanner;

public class DispatchService {
    public static String name = null;
    private static ChannelFuture channelFuture = null;
    private static Config config = Config.getConfig();

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientUserService clientUserService = new ClientUserServiceImpl();


    public static boolean start(){
        try {
            channelFuture=NettyClient.StartClient(config.getIp(),config.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean stop(){
        channelFuture.channel().closeFuture();
        return true;
    }

    /**
     * 登录
     */
    public static boolean doLogin(){
        if(channelFuture==null){
            start();
        }
        System.out.println("用户名:");
        String name = scanner.nextLine();
        System.out.println("密码:");
        String password = scanner.nextLine();
        return clientUserService.Login(channelFuture.channel(),name,password);
    }

    /**
     * 注册
     * @return
     */
    public static boolean doRegister() {
        if(channelFuture==null){
            start();
        }
        System.out.println("请输入用户名:");
        String name = scanner.nextLine();
        System.out.println("请输入密码");
        String password = scanner.nextLine();
        System.out.println("请输入email");
        String email = scanner.nextLine();
        return clientUserService.Register(channelFuture.channel(),name,password,email);
    }

    /**
     * 忘记密码
     * @return
     */
    public static boolean doForgetPassWord() {
        if(channelFuture==null){
            start();
        }
        System.out.println("请输入用户名");
        String name = scanner.nextLine();
        System.out.println("请输入email");
        String email = scanner.nextLine();
        return clientUserService.ForgetPassWord(channelFuture.channel(),name,email);
    }


    public static boolean doModifyPassWord(){
        System.out.println("请输入原密码");
        String oldPassword = scanner.nextLine();
        System.out.println("请输入新密码");
        String newPassword = scanner.nextLine();
        return clientUserService.ModifyPassWord(channelFuture.channel(),oldPassword,newPassword);
    }


    public static boolean doGetAllUsers() {
        try {
            return clientUserService.GetAllUsers(channelFuture.channel());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean doQuit() {
        return clientUserService.Quit(channelFuture.channel());
    }

    public static boolean doSendUserMsg(String Toname, String msg) {
        return clientUserService.SendUserMsg(channelFuture.channel(),Toname,msg);
    }

    public static boolean doSendAllUserMsg(String msg) {
        return clientUserService.SendAllUserMsg(channelFuture.channel(),msg);
    }

    public static boolean doSendUserFile(String name, String path) {
        return clientUserService.SendUserFile(channelFuture.channel(),name,path);
    }
}
