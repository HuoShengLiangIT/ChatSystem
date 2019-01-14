package cn.chenmixuexi.controller;

import java.io.*;
import java.net.Socket;

public class FileRecvHandler implements Runnable {
    private Socket recvClient;
    public FileRecvHandler(String ip,int port){
        try {
            recvClient = new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDeafultPath(){
        String path = FileRecvHandler.class.getResource("").getPath();
        int i = path.indexOf("/target");
        return path.substring(0,i);
    }

    public void run(){
        DataInputStream dataInputStream=null;
        FileOutputStream fileOutputStream=null;
        try {
            dataInputStream = new DataInputStream(recvClient.getInputStream());
            String name = dataInputStream.readUTF();
            long length = dataInputStream.readLong();
            System.out.println("【接受方】: name:"+name+"  length:"+length);
            String pathName = getDeafultPath()+ File.separator+name;
            System.out.println(pathName);
            fileOutputStream = new FileOutputStream(pathName);
            int len;
            byte[] bytes = new byte[1024];
            while ((len=dataInputStream.read(bytes,0,bytes.length))!=-1){
                fileOutputStream.write(bytes,0,len);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dataInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
