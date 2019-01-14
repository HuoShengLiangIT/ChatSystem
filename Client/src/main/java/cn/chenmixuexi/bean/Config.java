package cn.chenmixuexi.bean;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {
    private static final Config config = new Config();
    private int port;
    private String ip;
    private Config(){
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Config.class.getClassLoader().getResourceAsStream("Config.properties"),"utf-8"));
            port= Integer.parseInt(properties.getProperty("port"));
            ip=properties.getProperty("ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public static Config getConfig() {
        return config;
    }
}
