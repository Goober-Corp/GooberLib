package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Tweener {
    //TODO: add easings
    Supplier<Number> target;
    double value;

    public Tweener(Supplier<Number> target) {
        this.target = target;
		value = target.get().doubleValue();
    }

    public void update() {
        value = RenderUtils.ease(value, target.get().doubleValue(), 15);
    }

    public double get() {
        return value;
    }

    public double getLerped(double start, double end) {
        return MathHelper.lerp(value, start, end);
    }
}
