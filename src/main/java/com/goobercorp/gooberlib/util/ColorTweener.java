package com.goobercorp.gooberlib.util;

import net.minecraft.util.math.ColorHelper;

import java.util.function.Supplier;

public class ColorTweener {

	Supplier<Integer> target;
	Tweener aTweener;
	Tweener rTweener;
	Tweener gTweener;
	Tweener bTweener;
	float speed;


	public ColorTweener(Supplier<Integer> target, float speed) {
		this.target = target;
		this.speed = speed;
		int yeah = target.get();
		this.aTweener = new Tweener(() -> ColorHelper.getAlpha(yeah) / 255F, speed);
		this.rTweener = new Tweener(() -> ColorHelper.getRed(yeah) / 255F, speed);
		this.gTweener = new Tweener(() -> ColorHelper.getGreen(yeah) / 255F, speed);
		this.bTweener = new Tweener(() -> ColorHelper.getBlue(yeah) / 255F, speed);
	}

	public void update() {
		aTweener.update();
		rTweener.update();
		gTweener.update();
		bTweener.update();
	}

	public int get() {
		return ColorHelper.fromFloats(aTweener.getF(), rTweener.getF(), gTweener.getF(), bTweener.getF());
	}

}
