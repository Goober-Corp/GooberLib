package com.goobercorp.gooberlib.util;

import net.minecraft.util.Mth;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScrollTweener {
	Supplier<Double> target;
	// this sucks. we don't talk about it because it works
	Consumer<Double> targetWriter;
	public boolean isBeingInteractedWith = false;
	double value;
	public double min;
	public double max;
	private float speed;

	public ScrollTweener(Supplier<Double> target, Consumer<Double> targetWriter, double min, double max, float speed) {
		this.target = target;
		this.targetWriter = targetWriter;
		value = target.get();
		this.min = min;
		this.max = max;
		this.speed = speed;
	}

	public ScrollTweener(Supplier<Double> target, Consumer<Double> targetWriter, double min, double max) {
		this(target, targetWriter, min, max, 20);
	}

	public void setInteractionState(boolean yeah) {
		this.isBeingInteractedWith = yeah;
	}

	public void update() {
		if (!isBeingInteractedWith) {
			targetWriter.accept(RenderUtils.ease(target.get(), Math.clamp(target.get(), min, max), 15));
		}
		value = RenderUtils.ease(value, target.get(), speed);
	}

	public void snap() {
		value = target.get();
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

	public double getLerped(double start, double end) {
		return Mth.lerp(value, start, end);
	}

	public double getUnlerped() {
		return (value - min) / (max - min);
	}
}
