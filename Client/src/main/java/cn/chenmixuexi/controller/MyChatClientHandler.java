package cn.chenmixuexi.controller;

import cn.chenmixuexi.bean.Config;
import cn.chenmixuexi.contant.MsgType;
import cn.chenmixuexi.service.DispatchService;
import cn.chenmixuexi.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class MyChatClientHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 消息队列
     */
    public static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    private static Config config = Config.getConfig();
    public static Executor executor = Executors.newCachedThreadPool();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        JsonNode typestring = objectNode.get("type");
        if(typestring!=null){
            MsgType type = MsgType.valueOf(typestring.asText());
            switch (type){
                case MSG_CHAT:{
                    System.out.println("一对一聊天:"+objectNode.get("name")+":"+objectNode.get("msg"));
                    break;
                }
                case MSG_CHAT_ALL:{
                    System.out.println("群发:"+objectNode.get("name")+":"+objectNode.get("msg"));
                    break;
                }
                case MSG_OFFLINE_MSG_EXIST:{
                    System.out.println("离线消息:"+objectNode.get("name")+":"+objectNode.get("msg"));
                    break;
                }
                case MSG_TRANSFER_FILE:{
                    JsonNode name = objectNode.get("name");
                    if(name!=null){
                        System.out.println(name+" 发来文件!");
                        executor.execute(new FileRecvHandler(config.getIp(),objectNode.get("port").asInt()));
                    }else{
                        queue.add(msg);
                    }
                    break;
                }
                case MSG_OFFLINE_FILE_EXIST:{
                    JsonNode name = objectNode.get("name");
                    if(name!=null){
                        System.out.println(name+" 发来离线文件!");
                        executor.execute(new FileRecvHandler(config.getIp(),objectNode.get("port").asInt()));
                    }
                    break;
                }
            }
        }else{
            queue.add(msg);
        }

    }
}
