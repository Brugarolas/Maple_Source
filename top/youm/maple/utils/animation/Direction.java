package top.youm.maple.utils.animation;

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