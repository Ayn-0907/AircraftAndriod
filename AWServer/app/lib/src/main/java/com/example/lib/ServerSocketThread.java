package com.example.lib;


import static com.example.lib.MyClass.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerSocketThread extends Thread{
    private BufferedReader in, in_0, in_1;
    private PrintWriter pw;
    private boolean isConnect = false;
    private Socket socket;
    public ServerSocketThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

            pw = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
            //4.和客户端通信
            String content_0, content_1 = null;
            while (true) {
                if (!isConnect && sockets.size() == 2){
                    this.sendMessage(sockets.get(0),"2");
                    this.sendMessage(sockets.get(1),"2");
                    in_0 = new BufferedReader(new InputStreamReader(sockets.get(0).getInputStream(),"UTF-8"));
                    in_1 = new BufferedReader(new InputStreamReader(sockets.get(1).getInputStream(),"UTF-8"));
                    isConnect = true;
                }
                if (isConnect) {
                    if ((content_0 = in_0.readLine()) != null) {
                        this.sendMessage(sockets.get(1),content_0);
                    }
                    if ((content_1 = in_1.readLine()) != null) {
                        this.sendMessage(sockets.get(0),content_1);
                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(Socket socket, String message) {
        PrintWriter pout = null;
        try{
            System.out.println("message to client:" + message);
            System.out.println(sockets.size());
            pout = new PrintWriter(
                   new BufferedWriter(
                   new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
            pout.println(message);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
