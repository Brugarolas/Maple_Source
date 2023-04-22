package me.youm.rocchi.common.irc.packet;

import me.youm.rocchi.common.irc.Player;

import java.io.Serializable;

public class IRC02ChatPacket implements Serializable {
    private String message;
    private Player player;

    public IRC02ChatPacket() {
    }

    public IRC02ChatPacket(String message, Player player) {
        this.message = message;
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
