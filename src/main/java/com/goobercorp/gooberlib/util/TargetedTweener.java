package com.goobercorp.gooberlib.util;

public class TargetedTweener extends Tweener {
	public Number targetV = 0;

	public TargetedTweener() {
		this(15);
	}

	public TargetedTweener(float speed) {
		super(() -> 0, speed);
		target = () -> targetV;
	}

	public void setTarget(Number targetValue) {
		this.targetV = targetValue;
		if (targetV == null) targetV = 0;
	}
}
