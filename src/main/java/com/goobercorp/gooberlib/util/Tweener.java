package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Tweener {
	//TODO: add easings
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

	public boolean isAtTarget() {
		return Math.abs(target.get().doubleValue() - value) < 0.001;
	}

	public double getLerped(double start, double end) {
		return MathHelper.lerp(value, start, end);
	}
}
