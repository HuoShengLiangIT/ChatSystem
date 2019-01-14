package cn.chenmixuexi;

import cn.chenmixuexi.bean.Config;
import cn.chenmixuexi.controller.ClientController;
import cn.chenmixuexi.controller.NettyClient;

import java.io.IOException;

/**
 *Client start main
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        ClientController clientController = new ClientController();
        clientController.run();
    }
}
