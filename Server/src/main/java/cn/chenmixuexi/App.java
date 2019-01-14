package cn.chenmixuexi;

import cn.chenmixuexi.bean.Config;
import cn.chenmixuexi.controller.NettyServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        NettyServer.StartServer(Config.getConfig().getPort());
    }
}
