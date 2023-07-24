package me.youm.test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class Main{
    public static void main(String[] args) throws IOException, FontFormatException {
        InputStream resource = Objects.requireNonNull(Main.class.getClassLoader().getResource("assets/minecraft/Rocchi/font/wqy_microhei.ttf")).openStream();
        Font.createFont(0, resource);
    }
}