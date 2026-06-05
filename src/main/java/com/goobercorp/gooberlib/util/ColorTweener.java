package com.goobercorp.gooberlib.util;

import java.util.function.Supplier;

import net.minecraft.util.ARGB;

public class ColorTweener {

	Supplier<Integer> target;
	Tweener aTweener;
	Tweener rTweener;
	Tweener gTweener;
	Tweener bTweener;


	public ColorTweener(Supplier<Integer> target, float speed) {
		this.target = target;
		this.aTweener = new Tweener(() -> ARGB.alpha(this.target.get()) / 255F, speed);
		this.rTweener = new Tweener(() -> ARGB.red(this.target.get()) / 255F, speed);
		this.gTweener = new Tweener(() -> ARGB.green(this.target.get()) / 255F, speed);
		this.bTweener = new Tweener(() -> ARGB.blue(this.target.get()) / 255F, speed);
	}

	public void update() {
		aTweener.update();
		rTweener.update();
		gTweener.update();
		bTweener.update();
	}

	public int get() {
		return ARGB.colorFromFloat(aTweener.getF(), rTweener.getF(), gTweener.getF(), bTweener.getF());
	}

}
