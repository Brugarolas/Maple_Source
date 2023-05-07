package top.youm.rocchi.common.irc.packet;

import java.io.Serializable;

public class S01ConnectPacket implements Serializable {
    private boolean type;

    public S01ConnectPacket(boolean type) {
        this.type = type;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
