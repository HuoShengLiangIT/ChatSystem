package cn.chenmixuexi.service.impl;

import cn.chenmixuexi.bean.Config;
import cn.chenmixuexi.contant.MsgType;
import cn.chenmixuexi.controller.FileSendHandler;
import cn.chenmixuexi.controller.MyChatClientHandler;
import cn.chenmixuexi.service.ClientUserService;
import cn.chenmixuexi.service.DispatchService;
import cn.chenmixuexi.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.Channel;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ClientUserServiceImpl implements ClientUserService {
    @Override
    public boolean Login(Channel channel, String name, String password) {
        ObjectNode data = JsonUtil.getObjectNode();
        data.put("type", MsgType.MSG_LOGIN.toString());
        data.put("name",name);
        data.put("password",password);
        SendJson(data,channel);
        String wait = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(wait);
        Integer code = resultJson.get("code").asInt();
        if(code==200){
            DispatchService.name = name;
            return true;
        }
        if(code==500){
            System.err.println("请检查用户名或密码");
        }
        return false;
    }

    @Override
    public boolean Register(Channel channel, String name, String password, String email) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_REGISTER.toString());
        objectNode.put("name",name);
        objectNode.put("password",password);
        objectNode.put("email",email);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean ForgetPassWord(Channel channel, String name, String email) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_FORGET_PWD.toString());
        objectNode.put("name",name);
        objectNode.put("email",email);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean ModifyPassWord(Channel channel, String oldPassword, String newPassword) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_MODIFY_PWD.toString());
        objectNode.put("name",DispatchService.name);
        objectNode.put("oldPassword",oldPassword);
        objectNode.put("newPassword",newPassword);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean GetAllUsers(Channel channel) throws IOException {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_GET_ALL_USERS.toString());
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        System.out.println(resultJson);
        int code = resultJson.get("code").asInt();
        if(code==200){
            String userlist = resultJson.get("list").asText();
            Collection collection = JsonUtil.JsonForCollection(userlist);
            System.out.println("当前在线用户为：");
            for (Object string :
                    collection) {
                System.out.println(string);
            }
            System.out.println("=========================");
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean Quit(Channel channel) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_OFFLINE.toString());
        objectNode.put("name",DispatchService.name);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean SendUserMsg(Channel channel, String toname, String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_CHAT.toString());
        objectNode.put("name",DispatchService.name);
        objectNode.put("toname",toname);
        objectNode.put("msg",msg);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }else if(code==300){
            System.out.println("用户名不存在");
        }else if(code==100){
            System.out.println("用户不在线，已发送成离线消息");
            return true;
        }
        return false;
    }

    @Override
    public boolean SendAllUserMsg(Channel channel, String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_CHAT_ALL.toString());
        objectNode.put("name",DispatchService.name);
        objectNode.put("msg",msg);
        objectNode.put("flag",true);
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            return true;
        }
        return false;
    }

    @Override
    public boolean SendUserFile(Channel channel, String name, String path) {
        File file = new File(path);
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("type",MsgType.MSG_TRANSFER_FILE.toString());
        objectNode.put("name",DispatchService.name);
        objectNode.put("toname",name);
        objectNode.put("filename",file.getName());
        SendJson(objectNode,channel);
        String s = WaitJson();
        ObjectNode resultJson = JsonUtil.getObjectNode(s);
        int code = resultJson.get("code").asInt();
        if(code==200){
            s = WaitJson();
            ObjectNode fileJson = JsonUtil.getObjectNode(s);
            System.out.println("sendFileJson："+fileJson);
            int port = fileJson.get("port").asInt();
            new FileSendHandler(Config.getConfig().getIp(), port).sendFile(file);
            return true;
        }else if(code==300){
            System.out.println("用户名不存在");
        }else if(code==100){
            s = WaitJson();
            ObjectNode fileJson = JsonUtil.getObjectNode(s);
            System.out.println("sendFileJson："+fileJson);
            int port = fileJson.get("port").asInt();
            new FileSendHandler(Config.getConfig().getIp(), port).sendFile(file);
            System.out.println("用户不在线，已发送成离线文件");
            return true;
        }
        return false;
    }

    private void SendJson(ObjectNode objectNode,Channel channel){
        channel.writeAndFlush(objectNode.toString()+"\n");
    }

    private String WaitJson(){
        String result = null;
        try {
            result = MyChatClientHandler.queue.poll(120,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("超时");
            return null;
        }
        return result;
    }
}
