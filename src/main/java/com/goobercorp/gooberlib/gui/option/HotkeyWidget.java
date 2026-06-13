package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import java.util.Arrays;
import java.util.List;

public class HotkeyWidget extends EvilBaseWidget {
	HotkeyOption opt;
	private final Font font = Minecraft.getInstance().font;
	private boolean isListening;
	private boolean shouldClear;

	public HotkeyWidget(HotkeyOption opt, int x, int y, int width, int height) {
		super(opt.name(), x, y, width, height);
		this.opt = opt;
		this.shouldDrawName = true;
	}

	@Override
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
		String text = "";
		if (isListening) text += "> ";
		if (!shouldClear) text += opt.keyCodesString();
		if (isListening) text += " <";
		context.drawString(font, text, this.getX() + this.width - font.width(text) - 5, this.getY() + 4, MainConfig.primaryCol);

		opt.editing = isListening;
	}

	@Override
	public void onLostFocus() {
		this.shouldClear = false;
		this.isListening = false;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
		if (this.isActive()) {
			boolean mouseOver = this.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y());
			if (mouseOver) {
				this.playDownSound(Minecraft.getInstance().getSoundManager());
				if (isListening) {
					if (shouldClear) {
						opt.clearKeyCodes();
						shouldClear = false;
					}
					opt.addCode(mouseButtonEvent.button());
				} else {
					shouldClear = true;
					isListening = true;
				}

				return true;
			}
		}
		return false;
	}

	private List<Integer> prevKeycodes;

	@Override
	public boolean keyPressed(KeyEvent keyEvent) {
		if (isListening) {
			if (keyEvent.isEscape()) {
				if (shouldClear) {
					prevKeycodes = Arrays.stream(opt.keyCodes).boxed().toList();
					opt.clearKeyCodes();
					shouldClear = false;
				} else {
					if (prevKeycodes != null) {
						opt.clearKeyCodes();
						for (int code : prevKeycodes) {
							opt.addCode(code);
						}
					}
				}
				isListening = false;
				return true;
			}
			if (shouldClear) {
				prevKeycodes = Arrays.stream(opt.keyCodes).boxed().toList();
				opt.clearKeyCodes();
				shouldClear = false;
			}
			opt.addCode(keyEvent.key());
			return true;
		}
		return false;
	}
}
