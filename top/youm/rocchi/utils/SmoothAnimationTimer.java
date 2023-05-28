package top.youm.rocchi.utils;

public class SmoothAnimationTimer {
	public float target;
	public float speed = 0.3f;
	public SmoothAnimationTimer(float target) {
		this.target = target;
	}
	public SmoothAnimationTimer(float target, float speed) {
		this.target = target;
		this.speed = speed;
	}
	private float value = 0;
    public boolean update(boolean increment) {
    	this.value = Animation.getAnimationState(value, increment ? target : 0, (float) (Math.max(10, (Math.abs(this.value - (increment ? target : 0))) * 40) * speed));
    	return value == target;
    }
	public void setValue(float value) {
		this.value = value;
	}
	public void setTarget(float scrollY) {
		this.target = scrollY;
	}
	public int getValue() {
		return (int) value;
	}
	public float getTarget() {
		return target;
	}
}