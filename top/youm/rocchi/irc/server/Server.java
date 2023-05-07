package top.youm.rocchi.irc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static ServerSocket serverSocket = null;

    static {
        try {
            serverSocket = new ServerSocket(22345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ;
    public static final List<Socket> sockets = new ArrayList<>();
    public static void run(){
        boolean flag = true;
        //接受客户端请求
        while (flag){
            try {
                Socket accept = serverSocket.accept();
                synchronized (sockets) {
                    sockets.add(accept);
                }
                new Thread(new ServerThead(accept)).start();
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){
        run();
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
