package top.youm.rocchi.core.ui.clickgui.modern.animation;

public enum Direction
{
    FORWARDS, 
    BACKWARDS;
    public Direction opposite() {
        if (this == Direction.FORWARDS) {
            return Direction.BACKWARDS;
        }
        return Direction.FORWARDS;
    }
}