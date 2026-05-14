package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScrollTweener {
	//TODO: this sucks. we don't talk about it because it works
	Supplier<Double> target;
	Consumer<Double> targetWriter;
	double value;
	double min;
	double max;


	public ScrollTweener(Supplier<Double> target, Consumer<Double> targetWriter, double min, double max) {
		this.target = target;
		this.targetWriter = targetWriter;
		value = target.get();
		this.min = min;
		this.max = max;
	}

	public void update() {
		targetWriter.accept(RenderUtils.ease(target.get(), Math.clamp(target.get(), min, max), 15));
		value = RenderUtils.ease(value, target.get(), 15);
	}

	public double get() {
		return value;
	}

	public double getLerped(double start, double end) {
		return MathHelper.lerp(value, start, end);
	}
}
