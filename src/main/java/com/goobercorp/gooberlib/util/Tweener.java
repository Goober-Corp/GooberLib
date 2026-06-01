package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Tweener {
	//TODO: add easings
	//TODO: have some sort of common interface for Tweener and ScrollTweener
	Supplier<Number> target;
	double value;
	float speed;

	public Tweener(Supplier<Number> target) {
		this(target, 15);
	}

	public Tweener(Supplier<Number> target, float speed) {
		this.target = target;
		value = target.get().doubleValue();
		this.speed = speed;
	}

	public void update() {
		value = RenderUtils.ease(value, target.get().doubleValue(), speed);
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
		return MathHelper.approximatelyEquals(value, target.get().doubleValue());
	}

	public double getLerped(double start, double end) {
		return MathHelper.lerp(value, start, end);
	}
}
