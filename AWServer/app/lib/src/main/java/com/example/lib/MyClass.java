package com.example.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyClass {
    public static ArrayList<Socket> sockets = new ArrayList<Socket>();
    public static void main(String[] args){
        new MyClass();
    }

    public MyClass(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);
            // 1. 创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--Listener Port: 9999--");
            while (true) {
                //2.等待接收请求，这里接收客户端的请求
                Socket client = serverSocket.accept();
                if (sockets.size() == 2) {
                    sockets = new ArrayList<Socket>();
                }
                sockets.add(client);
                System.out.println("accept client connect" + client);
                //3.开启子线程线程处理和客户端的消息传输
                new ServerSocketThread(client).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}