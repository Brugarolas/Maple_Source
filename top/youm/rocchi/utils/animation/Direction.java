package top.youm.rocchi.utils.animation;

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