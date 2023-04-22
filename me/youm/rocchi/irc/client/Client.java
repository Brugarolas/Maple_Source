package me.youm.rocchi.irc.client;


import me.youm.rocchi.common.irc.Player;

import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;

public class Client {
    private final Player player;
    private final ReadTask readTask = new ReadTask();
    public Socket socket;
    public Client() {
        this.player = new Player();
        try {
            this.socket = new Socket("localhost",22345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void connect() throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        this.run();
    }
    public void run(){
        new Thread(readTask, "ReadTask").start();

    }

    public void close(){

    }
    public void send(String name,String message){

    }

    static class ReadTask implements Runnable{

        @Override
        public void run() {

        }
    }
}
