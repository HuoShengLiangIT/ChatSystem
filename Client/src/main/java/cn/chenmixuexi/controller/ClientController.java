package cn.chenmixuexi.controller;

import cn.chenmixuexi.service.DispatchService;

import java.io.File;
import java.util.Scanner;

/**
 * 客户端的具体业务
 * 2018年11月26日21:07:55
 */
public class ClientController {
    private Scanner scanner = new Scanner(System.in);

    /**
     * 启动
     */
    public void run(){
        while (true){
            welcome();
            String line = scanner.nextLine();
            int choice=0;
            try{
                choice= Integer.parseInt(line);
            }catch (Exception e){
                System.err.println("请输入有效字符");
            }
            switch (choice){
                case 1:{
                    //登录
                    boolean b = DispatchService.doLogin();
                    //如果登录成功
                    if(b){
                        //进入菜单循环
                        while (menu()){
                        }
                    }
                    break;
                }
                case 2:{
                    //注册
                    if(DispatchService.doRegister()){
                        System.out.println("注册成功");
                    }else{
                        System.out.println("注册失败");
                    }
                    break;
                }
                case 3:{
                    //忘记密码
                    if(DispatchService.doForgetPassWord()){
                        System.out.println("邮件发送成功");
                    }else {
                        System.out.println("邮件发送失败");
                    }
                    break;
                }
                case 4:{
                    //退出系统
                    DispatchService.stop();
                    System.exit(0);
                    break;
                }
                default:{
                    //有误
                    System.out.println("命令有误，请重新选择");
                    break;
                }
            }
        }
    }
    /**
     * 登录成功后的主页
     */
    private boolean menu() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("1.输入modifypwd表示该用户要修改密码\n")
                .append("2.输入getallusers:表示该用户要查询所有的在线人员信息\n")
                .append("3.输入username:表示一对一聊天\n")
                .append("4.输入all:xxx表示发送群聊\n")
                .append("5.输入sendfile:username:xxx表示发送文件请求\n")
                .append("6.输入quit 表示该用户下线,退出系统");
        System.out.println(stringBuilder);
        String s = scanner.nextLine();
        if(s.equals("quit")){
            if(DispatchService.doQuit()){
                System.out.println("退出成功");
                return false;
            }else{
                System.out.println("退出失败");
            }
            return true;
        }else if(s.equals("modifypwd")){
            if(DispatchService.doModifyPassWord()){
                System.out.println("更改密码成功");
            }else{
                System.out.println("更改密码失败");
            }
            return true;
        } else if(s.equals("getallusers")){
            if(DispatchService.doGetAllUsers()){
                System.out.println("获取用户成功");
            }else{
                System.out.println("获取用户失败");
            }
            return true;
        }
        String[] split = s.split(":");
        String action = split[0];
        if(action.equals("all")){
            if(DispatchService.doSendAllUserMsg(split[1])){
                System.out.println("发送成功");
            }else {
                System.out.println("发送失败");
            }
        }else if(action.equals("sendfile")){
            String name = split[1];
            StringBuilder path = new StringBuilder();
            for (int i = 2; i < split.length; i++) {
                path.append(split[i]);
                if(i!=split.length-1){
                    path.append(":");
                }
            }
            File file = new File(path.toString());
            System.out.println(file.getPath());
            if(!file.exists()||!file.isFile()){
                System.out.println("文件不合法");
                return true;
            }
            if(DispatchService.doSendUserFile(name,path.toString())){
                System.out.println("发送成功");
            }else{
                System.out.println("发送失败");
            }
        }else{
            if(split.length!=2){
                System.out.println("命令错误");
                return true;
            }
            if(DispatchService.doSendUserMsg(split[0],split[1])){
                System.out.println("发送成功");
            }else{
                System.out.println("发送失败");
            }
        }
        return true;
    }

    /**
     * 用户页面
     */
    public void welcome(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==================\n")
                .append("1.登录\n")
                .append("2.注册\n")
                .append("3.忘记密码\n")
                .append("4.退出系统\n")
                .append("==================\n")
                .append("请输入命令选项:");
        System.out.println(stringBuilder.toString());
    }







}
