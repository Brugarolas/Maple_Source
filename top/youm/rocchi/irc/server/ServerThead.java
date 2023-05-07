package top.youm.rocchi.irc.server;

import top.youm.rocchi.irc.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThead implements Runnable {
    Socket socket;
    String socketName;
    MessageHandler handler = new MessageHandler();
    public ServerThead(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketName = socket.getRemoteSocketAddress().toString();
            boolean flag = true;
            while (flag)
            {
                String line = reader.readLine();
                handler.handle(line);
            }

            closeConnect();
        } catch (IOException e) {
            try {
                closeConnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    public void closeConnect() throws IOException {
        synchronized (Server.sockets){
            Server.sockets.remove(socket);
        }
        socket.close();
    }
}
