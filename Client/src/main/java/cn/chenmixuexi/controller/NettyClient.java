package cn.chenmixuexi.controller;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NettyClient {
    public static ChannelFuture StartClient(String ip, int port) throws IOException, InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new MyChatClientInitializer());
//            Channel channel = bootstrap.connect(ip, port).sync().channel();
            return bootstrap.connect(ip, port).sync();
    }
}
