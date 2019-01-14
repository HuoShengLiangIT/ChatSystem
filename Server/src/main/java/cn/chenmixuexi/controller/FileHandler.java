package cn.chenmixuexi.controller;

import cn.chenmixuexi.bean.OfflineMsg;
import cn.chenmixuexi.service.UserService;
import cn.chenmixuexi.service.impl.UserServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileHandler implements Runnable {
    public static UserService userService = new UserServiceImpl();
    private Integer toPort;
    private Integer fromPort;
    ServerSocket toServerSocket=null;
    ServerSocket fromServerSocket=null;
    File file;
    public FileHandler(Integer fromPort, Integer toPort,File file){
        this.fromPort = fromPort;
        this.toPort = toPort;
        try {
            if(this.toPort!=null){
                toServerSocket = new ServerSocket(toPort);
            }
            if(this.fromPort!=null){
                fromServerSocket = new ServerSocket(fromPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = file;
    }

    public static String getDeafultPath(){
        String path = FileHandler.class.getResource("").getPath();
        int i = path.indexOf("/target");
        return path.substring(0,i);
    }


    @Override
    public void run() {
        if(toServerSocket!=null&&fromServerSocket!=null){
            try {
                Socket fromClient = fromServerSocket.accept();
                Socket toClient = toServerSocket.accept();

                DataInputStream dataInputStream = new DataInputStream(fromClient.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(toClient.getOutputStream());
                int len;
                byte[] bytes = new byte[1024];
                while ((len = dataInputStream.read(bytes,0,bytes.length))!=-1){
                    dataOutputStream.write(bytes,0,len);
                    dataOutputStream.flush();
                }
                fromClient.close();
                toClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //发送离线文件
        if(toServerSocket!=null&&fromServerSocket==null){
            FileInputStream fileInputStream = null;
            DataOutputStream dataOutputStream = null;
            try {
                Socket toClient = toServerSocket.accept();
                System.out.println("【服务器发送离线文件】: name:"+file.getName()+"  length:"+file.length());
                fileInputStream = new FileInputStream(file);
                dataOutputStream = new DataOutputStream(toClient.getOutputStream());
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
        //接受离线文件
        if(toServerSocket==null&&fromServerSocket!=null){
            DataInputStream dataInputStream=null;
            FileOutputStream fileOutputStream=null;
            try {
                Socket fromClient = fromServerSocket.accept();
                dataInputStream = new DataInputStream(fromClient.getInputStream());
                String name = dataInputStream.readUTF();
                long length = dataInputStream.readLong();
                System.out.println("【服务器离线文件】: name:"+name+"  length:"+length);
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
}
