package cn.chenmixuexi.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class EmailUtils {
//	public static void sendEmail(String toEmail, String msg) throws Exception{
//		SimpleEmail email = new SimpleEmail();
//		email.setHostName("smtp.163.com");
//
//        email.setAuthentication("413246753@163.com", "Apptree2009");
//        email.setSSLOnConnect(true);
//        email.setFrom("413246753@163.com", "局域网聊天系统邮件");
//        email.setSubject("用户忘记密码邮件");
//        email.setCharset("UTF-8");
//        email.setMsg(msg);
//        email.addTo(toEmail);
//        email.send();
//	}
    private static  String HostName = null;
    private static  String UserName = null;
    private static  String PassWord = null;
    private static  String SendEmail = null;
    private static  String SendName = null;
    static {
        Properties properties = new Properties();
        System.out.println(new File("").getAbsolutePath());
    try {
        properties.load(new InputStreamReader(EmailUtils.class.getClassLoader().getResourceAsStream("email.properties"),"utf-8"));
        HostName=properties.getProperty("HostName");
        UserName=properties.getProperty("UserName");
        PassWord=properties.getProperty("PassWord");
        SendEmail=properties.getProperty("SendEmail");
        SendName=properties.getProperty("SendName");
        System.out.println("HostName:"+HostName+" UserName"+UserName+" PassWord:"+PassWord+" SendEmail:"+SendEmail+" SendName:"+SendName);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public static void sendEmail(String toEmail, String msg) {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(HostName);//邮件服务器

        email.setAuthentication(UserName, PassWord);//邮件登录用户名及密码
        email.setSSLOnConnect(true);
        try {
            email.setFrom(SendEmail, SendName);//发送方邮箱、发送方名称
            email.setSubject("局域网聊天密码找回");//主题名称
            email.setCharset("UTF-8");//设置字符集编码
            email.setMsg(msg);//发送内容
            email.addTo(toEmail);//接收方邮箱
            email.send();//发送方法
        } catch (EmailException e) {
            System.err.println("邮件发送失败");
        }
    }
}