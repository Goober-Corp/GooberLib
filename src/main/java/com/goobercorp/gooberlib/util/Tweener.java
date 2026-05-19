package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Tweener {
    //TODO: add easings
    Supplier<Number> target;
    double value;
    float speed = 15;

    public Tweener(Supplier<Number> target) {
        this.target = target;
        value = target.get().doubleValue();
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

    public double getLerped(double start, double end) {
        return MathHelper.lerp(value, start, end);
    }
}
