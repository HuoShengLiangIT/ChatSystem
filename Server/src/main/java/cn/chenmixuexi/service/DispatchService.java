package cn.chenmixuexi.service;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.bean.User;
import cn.chenmixuexi.contant.MsgType;
import cn.chenmixuexi.controller.FileHandler;
import cn.chenmixuexi.controller.MyChatServerHandler;
import cn.chenmixuexi.service.impl.OfflineServiceImpl;
import cn.chenmixuexi.service.impl.UserServiceImpl;
import cn.chenmixuexi.util.JedisUtils;
import cn.chenmixuexi.util.JsonUtil;
import cn.chenmixuexi.util.PortUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.Channel;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DispatchService {
    public static UserService userService = new UserServiceImpl();
    public static OfflineService offlineService = new OfflineServiceImpl();
    public static HashMap<String,Channel> nameToChannel = new HashMap<>();
    public static HashMap<Channel,String> channelToname = new HashMap<>();
    public static Executor executor = Executors.newCachedThreadPool();

    public static void loginMap(String name,Channel channel){
        nameToChannel.put(name,channel);
        channelToname.put(channel,name);
    }

    public static void logoutMap(String name,Channel channel){
        nameToChannel.remove(name);
        channelToname.remove(channel);
    }

    public static void Operation(ObjectNode data, Channel channel, String s){
        MsgType type = MsgType.valueOf(data.get("type").asText());
        //根据请求不同进行请求进行操作
        switch (type){
            case MSG_LOGIN :{
                String name = data.get("name").asText();
                String password = data.get("password").asText();
                ObjectNode objectNode = JsonUtil.getObjectNode();
                if(userService.Login(name,password)){
                    loginMap(name,channel);
//                    JedisUtils.set(jedis,name,channel.remoteAddress());
                    objectNode.put("code",200);
                }else {
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                if(objectNode.get("code").asInt()==200){
                    //群发上线消息
                    ObjectNode senLoginmsg = JsonUtil.getObjectNode();
                    senLoginmsg.put("type",MsgType.MSG_CHAT_ALL.toString());
                    senLoginmsg.put("name",name);
                    senLoginmsg.put("msg","我上线了");
                    senLoginmsg.put("flag",false);
                    Operation(senLoginmsg,channel,senLoginmsg.toString());
                }
                List<OfflineMsg> offlineMsgs = offlineService.getOfflineMsg(name);
                Iterator<OfflineMsg> iterator = offlineMsgs.iterator();
                while (iterator.hasNext()){
                    OfflineMsg next = iterator.next();
                    if (next.getState().equals(1)&&next.getMsgType().equals(1)) {
                        ObjectNode msg = JsonUtil.getObjectNode();
                        msg.put("type",MsgType.MSG_OFFLINE_MSG_EXIST.toString());
                        msg.put("name",next.getFromName());
                        msg.put("msg",next.getMsg());
                        channel.writeAndFlush(msg+"\n");
                        next.setState(2);
                        offlineService.updateOfflineMsg(next);
                    }
                    if (next.getState().equals(1)&&next.getMsgType().equals(2)) {
                        Channel toChannel= nameToChannel.get(name);
                        ObjectNode todata = JsonUtil.getObjectNode();
                        int toPort = PortUtils.getFreePort();
                        todata.put("type",MsgType.MSG_OFFLINE_FILE_EXIST.toString());
                        todata.put("port",toPort);
                        todata.put("name",next.getFromName());
                        toChannel.writeAndFlush(todata+"\n");
                        System.err.println(todata);
                        System.err.println(new File(next.getMsg()).getName());
                        executor.execute(new FileHandler(null,toPort,new File(next.getMsg())));
                        next.setState(2);
                        offlineService.updateOfflineMsg(next);
                    }

                }

                break;
            }
            case MSG_REGISTER:{
                String name = data.get("name").asText();
                String password = data.get("password").asText();
                String email = data.get("email").asText();
                ObjectNode objectNode = JsonUtil.getObjectNode();
                if(userService.Register(name,password,email)){
                    objectNode.put("code",200);
                }else{
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                break;
            }
            case MSG_FORGET_PWD:{
                String name = data.get("name").asText();
                String email = data.get("email").asText();
                System.err.println("name:"+name+" email:"+email);;
                ObjectNode objectNode = JsonUtil.getObjectNode();
                if(userService.ForgetPassWord(name,email)){
                    objectNode.put("code",200);
                }else{
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                break;
            }
            case MSG_MODIFY_PWD:{
                String name = data.get("name").asText();
                String oldPassword = data.get("oldPassword").asText();
                String newPassword = data.get("newPassword").asText();
                ObjectNode objectNode = JsonUtil.getObjectNode();
                if(userService.ModifyPassWord(name,oldPassword,newPassword)){
                    objectNode.put("code",200);
                }else{
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                break;
            }
            case MSG_GET_ALL_USERS:{
                ObjectNode objectNode = JsonUtil.getObjectNode();
                Set<String> userlist=userService.getAllUsers();
                System.out.println(userlist);
                if(userlist!=null){
                    objectNode.put("code",200);
                    String jsonlist = JsonUtil.CollectionForJson(userlist);
                    objectNode.put("list",jsonlist);
                }else{
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                break;
            }
            case MSG_OFFLINE:{
                ObjectNode objectNode = JsonUtil.getObjectNode();
                String name = data.get("name").asText();
                if(userService.OffLine(name)){
                    logoutMap(name,channel);
                    objectNode.put("code",200);
                }else{
                    objectNode.put("code",500);
                }
                channel.writeAndFlush(objectNode+"\n");
                if(objectNode.get("code").asInt()==200){
                    //群发下线消息
                    ObjectNode senLoginmsg = JsonUtil.getObjectNode();
                    senLoginmsg.put("type",MsgType.MSG_CHAT_ALL.toString());
                    senLoginmsg.put("name",name);
                    senLoginmsg.put("msg","我下线了，拜拜");
                    senLoginmsg.put("flag",false);
                    Operation(senLoginmsg,channel,senLoginmsg.toString());
                }
                break;
            }
            case MSG_CHAT:{
                //返回的数据包装类
                ObjectNode objectNode = JsonUtil.getObjectNode();
                //获取name toname msg三个属性
                String name = data.get("name").asText();
                String toname = data.get("toname").asText();
                String msg = data.get("msg").asText();
                //查询此用户是否存在
                User user = userService.getUser(toname);
                if(user!=null){
                    //查询此用户是否在线
                    Boolean is = userService.isUserOnline(toname);
                    if(is){
                        //获取接受方的channel
                        Channel to = nameToChannel.get(toname);
                        ObjectNode tomsg = JsonUtil.getObjectNode();
                        tomsg.put("type", MsgType.MSG_CHAT.toString());
                        tomsg.put("name",name);
                        tomsg.put("msg",msg);
                        System.out.println(msg+" "+toname);
                        SendJson(tomsg,to);
//                        String ack = WaitJson();
//                        ObjectNode ackjson = JsonUtil.getObjectNode(ack);
//                        String acktype = ackjson.get("type").asText();
                        objectNode.put("code",200);
                    }else{
                        System.out.println("不在线");
                        //不在线
                        OfflineMsg offlineMsg = new OfflineMsg(null,null,toname,name,1,msg,1);
                        boolean b = userService.addOffLineMsg(offlineMsg);
                        if(b){
                            objectNode.put("code",100);
                        }else {
                            objectNode.put("code",300);
                        }
                    }
                }else{
                    objectNode.put("code",300);
                }
                channel.writeAndFlush(objectNode+"\n");
                break;
            }
            case MSG_CHAT_ALL:{
                //返回的数据包装类
                ObjectNode objectNode = JsonUtil.getObjectNode();
                //获取name msg属性
                String name = data.get("name").asText();
                String msg = data.get("msg").asText();
                boolean flag = data.get("flag").asBoolean();
                Iterator<Channel> iterator = nameToChannel.values().iterator();
                int count = 0;
                while (iterator.hasNext()){
                    Channel next = iterator.next();
                    if(next==channel){
                        continue;
                    }
                    ObjectNode tomsg = JsonUtil.getObjectNode();
                    tomsg.put("type", MsgType.MSG_CHAT_ALL.toString());
                    tomsg.put("name",name);
                    tomsg.put("msg",msg);
                    SendJson(tomsg,next);
//                    String ack = WaitJson();
//                    ObjectNode ackjson = JsonUtil.getObjectNode(ack);
//                    String acktype = ackjson.get("type").asText();
//                    if(acktype.equals(MsgType.MSG_ACK.toString())){
//                        count++;
//                    }
                }
                if(flag){
                    objectNode.put("code",200);
                    channel.writeAndFlush(objectNode+"\n");
                }
                break;
            }
            case MSG_TRANSFER_FILE:{
                ObjectNode objectNode = JsonUtil.getObjectNode();
                //获取name msg属性
                String name = data.get("name").asText();
                String toname = data.get("toname").asText();
                String filename = data.get("filename").asText();
                //查询此用户是否存在
                User user = userService.getUser(toname);
                if(user!=null){
                    //查询此用户是否在线
                    Boolean is = userService.isUserOnline(toname);
                    if(is){
                        objectNode.put("code",200);
                    }else{
                        System.out.println("不在线");
                        //不在线
                        String pathName = FileHandler.getDeafultPath()+ File.separator+filename;
                        OfflineMsg offlineMsg = new OfflineMsg(null,null,toname,name,2,pathName,1);
                        boolean b = userService.addOffLineMsg(offlineMsg);
                        if(b){
                            objectNode.put("code",100);
                        }else {
                            objectNode.put("code",300);
                        }
                    }
                }else{
                    objectNode.put("code",300);
                }
                channel.writeAndFlush(objectNode+"\n");

                if(objectNode.get("code").asInt()==200){
                    int formPort = PortUtils.getFreePort();
                    Channel formChannel= nameToChannel.get(name);
                    ObjectNode formdata = JsonUtil.getObjectNode();
                    formdata.put("type",MsgType.MSG_TRANSFER_FILE.toString());
                    formdata.put("port",formPort);
                    formChannel.writeAndFlush(formdata+"\n");
                    int toPort = PortUtils.getFreePort();
                    Channel toChannel= nameToChannel.get(toname);
                    ObjectNode todata = JsonUtil.getObjectNode();
                    todata.put("type",MsgType.MSG_TRANSFER_FILE.toString());
                    todata.put("port",toPort);
                    todata.put("name",name);
                    toChannel.writeAndFlush(todata+"\n");
                    executor.execute(new FileHandler(formPort,toPort,null));
                }
                if(objectNode.get("code").asInt()==100){
                    int formPort = PortUtils.getFreePort();
                    Channel formChannel= nameToChannel.get(name);
                    ObjectNode formdata = JsonUtil.getObjectNode();
                    formdata.put("type",MsgType.MSG_TRANSFER_FILE.toString());
                    formdata.put("port",formPort);
                    formChannel.writeAndFlush(formdata+"\n");
                    executor.execute(new FileHandler(formPort,null,null));
                }
                break;
            }
        }
    }


    private static void SendJson(ObjectNode objectNode,Channel channel){
        channel.writeAndFlush(objectNode.toString()+"\n");
    }

    private static String WaitJson(){
        String result = null;
        try {
            result = MyChatServerHandler.queue.poll(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("超时");
            return null;
        }
        return result;
    }
}
