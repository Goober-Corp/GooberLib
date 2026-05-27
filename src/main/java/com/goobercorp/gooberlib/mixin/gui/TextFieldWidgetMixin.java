package com.goobercorp.gooberlib.mixin.gui;

import com.goobercorp.gooberlib.gui.EvilStringWidget;
import com.goobercorp.gooberlib.util.Tweener;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
	@Shadow
	private long lastSwitchFocusTime;
	@Unique
	private int targetCursorX;
	@Unique
	private final Tweener cursorTweener = new Tweener(() -> targetCursorX, 10);
	@Unique
	private boolean isFirstAfterAtTarget = false;

	@WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J"))
	private long inject2(Operation<Long> original) {
		if ((Object) this instanceof EvilStringWidget) {
			if (!cursorTweener.isAtTarget()) {
				isFirstAfterAtTarget = true;
				return this.lastSwitchFocusTime;
			} else if (isFirstAfterAtTarget) {
				isFirstAfterAtTarget = false;
				this.lastSwitchFocusTime = original.call();
			}
		}
		return original.call();
	}

	@WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
	private void inject(DrawContext instance, int i, int j, int k, int l, int m, Operation<Void> original) {
		if ((Object) this instanceof EvilStringWidget) {
			targetCursorX = i;
			cursorTweener.update();
			original.call(instance, (int) cursorTweener.get(), j, (int) cursorTweener.get() + 1, l, m);
			return;
		}
		original.call(instance, i, j, k, l, m);
	}

	@WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)V", slice = "one"), slice = @Slice(id = "one", from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V")))
	private void inject3(DrawContext instance, TextRenderer textRenderer, String string, int i, int j, int k, boolean bl, Operation<Void> original) {
		if ((Object) this instanceof EvilStringWidget) {
			targetCursorX = i;
			cursorTweener.update();
		}
		original.call(instance, textRenderer, string, i, j, k, bl);
	}

	@WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"))
	private void inject4(DrawContext instance, RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, Operation<Void> original) {
		if ((Object) this instanceof EvilStringWidget) {
			return;
		}
		original.call(instance, renderPipeline, identifier, i, j, k, l);
	}
}
