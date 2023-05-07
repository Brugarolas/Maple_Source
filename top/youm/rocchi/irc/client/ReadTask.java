package top.youm.rocchi.irc.client;

import top.youm.rocchi.Rocchi;
import top.youm.rocchi.irc.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadTask implements Runnable{

    MessageHandler handler = new MessageHandler();
    @Override
    public void run() {
        try {
            InputStream inputStream = Rocchi.getInstance().getClient().socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String message = bufferedReader.readLine();
            handler.handle(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}