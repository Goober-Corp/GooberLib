package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

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

		if (isListening && !isFocused()) {
			isListening = false;
		}
		opt.editing = isListening;
	}

	@Override
	public void setFocused(boolean bl) {
		super.setFocused(bl);
		if (!bl) {
			this.shouldClear = false;
			this.isListening = false;
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
		if (!this.isActive()) {
			return false;
		} else {
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

			return false;
		}
	}

	@Override
	public boolean keyPressed(KeyEvent keyEvent) {
		if (isListening) {
			if (shouldClear) {
				opt.clearKeyCodes();
				shouldClear = false;
			}
			opt.addCode(keyEvent.key());
		}
		return super.keyPressed(keyEvent);
	}
}
