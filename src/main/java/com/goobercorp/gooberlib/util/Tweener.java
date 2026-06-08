package com.goobercorp.gooberlib.util;

import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class Tweener {
	//TODO: add easings
	Supplier<Number> target;
	double value;
	float speed;

	public Tweener(Supplier<Number> target) {
		this(target, 20);
	}

	public Tweener(Supplier<Number> target, float speed) {
		this.target = target;
		value = target.get().doubleValue();
		this.speed = speed;
	}

	public void update() {
		value = RenderUtils.ease(value, target.get().doubleValue(), speed);
		if (isAtTarget()) value = target.get().doubleValue();
	}

	public double get() {
		return value;
	}

	public float getF() {
		return (float) value;
	}

	public int getI() {
		return (int) value;
	}

	public float getFloatingRemainder() {
		return this.getF() - (int) this.get();
	}

	public boolean isAtTarget() {
		// approxEq takes too long to say true
		var targetV = target.get();
		if (targetV == null) { // it was null at one point when it couldn't have been, and now I'm paranoid
			RenderUtils.breakpoint("targetV == null");
			return false;
		} else {
			return Math.abs(value - targetV.doubleValue()) < 1.0E-3F;
		}
	}

	public double getLerped(double start, double end) {
		return Mth.lerp(value, start, end);
	}

	public void snapToTarget() {
		this.value = this.target.get().doubleValue();
	}
}
