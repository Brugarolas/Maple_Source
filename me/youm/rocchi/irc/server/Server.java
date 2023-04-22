package me.youm.rocchi.irc.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    ServerSocket serverSocket;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(22345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
