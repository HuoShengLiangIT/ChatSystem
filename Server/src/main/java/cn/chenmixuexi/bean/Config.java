package cn.chenmixuexi.bean;

import cn.chenmixuexi.util.EmailUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置文件的初始化
 */
public class Config {
    private static Config config = new Config();
    private int port;

    private int Redis_port;
    private String Redis_ip;

    private Config(){
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(EmailUtils.class.getClassLoader().getResourceAsStream("Config.properties"),"utf-8"));
            port= Integer.parseInt(properties.getProperty("port"));
            Redis_port = Integer.parseInt(properties.getProperty("Redis_port"));
            Redis_ip = properties.getProperty("Redis_ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getPort() {
        return port;
    }

    public int getRedis_port() {
        return Redis_port;
    }

    public String getRedis_ip() {
        return Redis_ip;
    }

    public static Config getConfig() {
        return config;
    }
}
