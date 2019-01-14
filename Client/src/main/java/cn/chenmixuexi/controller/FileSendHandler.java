package cn.chenmixuexi.controller;

import java.io.*;
import java.net.Socket;

public class FileSendHandler  {
    private Socket client;
    private String ip;
    private int port;
    public FileSendHandler(String ip,int port){
        try {
            this.client = new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ip = ip;
        this.port = port;
    }

    public void sendFile(File file){
        FileInputStream fileInputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            System.out.println("【发送方】: name:"+file.getName()+"  length:"+file.length());
            fileInputStream = new FileInputStream(file);
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeLong(file.length());
            dataOutputStream.flush();
            int len;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes,0,bytes.length))!=-1){
                dataOutputStream.write(bytes,0,len);
                dataOutputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dataOutputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
