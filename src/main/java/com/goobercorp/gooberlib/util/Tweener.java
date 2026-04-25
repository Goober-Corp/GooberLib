package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Tweener {
    //TODO: add easings
    Supplier<Number> target;
    double value;

    public Tweener(Supplier<Number> target) {
        //uhm.
        this.target = target;
        try {
            value = target.get().doubleValue();
        } catch (Exception ignored) {
        }
        GooberLibEntrypoint.tweeners.add(this);
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
