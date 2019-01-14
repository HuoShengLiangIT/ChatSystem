package cn.chenmixuexi.controller;

import cn.chenmixuexi.contant.MsgType;
import cn.chenmixuexi.service.DispatchService;
import cn.chenmixuexi.service.UserService;
import cn.chenmixuexi.service.impl.UserServiceImpl;
import cn.chenmixuexi.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    //Channel组
//    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    private UserService userService = new UserServiceImpl();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        ObjectNode data = JsonUtil.getObjectNode(s);
        String type = data.get("type").asText();
        if(type.equals("MSG_ACK")){
            queue.add(s);
        }else{
            DispatchService.Operation(data,channel,s);
        }
    }





    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    //
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        String name = DispatchService.channelToname.get(ctx.channel());
        if(name==null){
            return;
        }
        ObjectNode senLoginmsg = JsonUtil.getObjectNode();
        senLoginmsg.put("type",MsgType.MSG_CHAT_ALL.toString());
        senLoginmsg.put("name",name);
        senLoginmsg.put("msg","我异常下线了。。。");
        senLoginmsg.put("flag",false);
        DispatchService.Operation(senLoginmsg,ctx.channel(),senLoginmsg.toString());

        userService.OffLine(name);
        DispatchService.logoutMap(name,ctx.channel());
        System.out.println("异常下线");
        if(!ctx.isRemoved()){
            ctx.close();
        }
    }
}
