package top.youm.rocchi.common.irc.packet;

import top.youm.rocchi.common.irc.Player;

import java.io.Serializable;

public class IRC02ChatPacket implements Serializable {
    private String message;
    private Player player;
    private Player receiver;

    public IRC02ChatPacket() {
    }

    public IRC02ChatPacket(String message, Player player, Player receiver) {
        this.message = message;
        this.player = player;
        this.receiver = receiver;
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

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }
}
