package me.youm.rocchi.irc.client;

import me.youm.rocchi.Rocchi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadTask implements Runnable{
    @Override
    public void run() {
        try {
            InputStream inputStream = Rocchi.getInstance().getClient().socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String message = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}