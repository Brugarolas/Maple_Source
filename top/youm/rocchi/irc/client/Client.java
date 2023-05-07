package top.youm.rocchi.irc.client;


import top.youm.rocchi.common.irc.Player;

import java.io.*;

import java.net.Socket;

public class Client {
    private final Player player;
    private final ReadTask readTask = new ReadTask();
    public Socket socket;
    public Client() {
        this.player = new Player();
        try {
            this.socket = new Socket("127.0.0.1",22345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void connect() throws IOException{
        this.run();
    }
    public void run(){
        new Thread(readTask, "ReadTask").start();
    }

    public void close(){

    }
    public void send(String name,String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(message);
        out.flush();
    }

}
