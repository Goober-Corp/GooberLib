package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EvilStringWidget extends EvilBaseWidget {
	public static final Style PLACEHOLDER_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
	private final Font textRenderer;
	private String text = "";
	private int maxLength = 32;
	private boolean drawsBackground = true;
	private boolean focusUnlocked = true;
	private boolean editable = true;
	private boolean centered = false;
	private boolean textShadow = true;
	private boolean invertSelectionBackground = true;
	private int firstCharacterIndex;
	private int selectionStart;
	private int selectionEnd;
	private int editableColor = MainConfig.primaryCol;
	private int uneditableColor = -9408400;
	@Nullable
	private String suggestion;
	@Nullable
	private Consumer<String> changedListener;
	private final Predicate<String> immediatePredicate;
	private final Predicate<String> predicate;
	@Nullable
	private Component placeholder;
	private long lastSwitchFocusTime = Util.getMillis();
	private int textX;
	private int textY;
	private String lastAccepted;
	private int targetCursorX = this.getX();
	private int targetCursorY = this.textY + 7;
	private int targetCursorWidth;
	private int targetCursorHeight;
	private int targetSelectionX1;
	private int targetSelectionX2;
	// todo: change tweener with .setTarget or something? and .setValue to set the value and not have tweening
	private final Tweener cursorXTweener = new Tweener(() -> targetCursorX, 10);
	private final Tweener cursorYTweener = new Tweener(() -> targetCursorY, 10);
	private final Tweener cursorWidthTweener = new Tweener(() -> targetCursorWidth, 10);
	private final Tweener cursorHeightTweener = new Tweener(() -> targetCursorHeight, 10);
	private final Tweener selectionX1Tweener = new Tweener(() -> targetSelectionX1, 10);
	private final Tweener selectionX2Tweener = new Tweener(() -> targetSelectionX2, 10);
	private boolean isFirstAfterAtTarget = false;
	private boolean firstAfterSelect = true;
	private boolean justFocused;
	private Function<String, Component> formatter = Component::literal;

	public EvilStringWidget(int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial) {
		super(Component.empty(), x, y, width, height);
		this.immediatePredicate = immediatePredicate;
		this.textRenderer = Minecraft.getInstance().font;
		this.lastAccepted = initial;
		this.setText(initial);
		this.setChangedListener(changedListener);
		this.predicate = predicate;
	}

	public EvilStringWidget(int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, int colorOverride) {
		this(x, y, width, height, changedListener, predicate, immediatePredicate, initial);
		//TODO: automatically calculate good selection color. blue text here means it's just white when selected.
		this.editableColor = colorOverride;
	}

	public void setChangedListener(Consumer<String> consumer) {
		this.changedListener = consumer;
	}

	@Override
	protected MutableComponent createNarrationMessage() {
		Component text = this.getMessage();
		return Component.translatable("gui.narrate.editBox", text, this.text);
	}

	public void setText(String string) {
		if (this.immediatePredicate.test(string)) {
			if (string.length() > this.maxLength) {
				this.text = string.substring(0, this.maxLength);
			} else {
				this.text = string;
			}

			this.setCursorToEnd(false);
			this.setSelectionEnd(this.selectionStart);
			this.onChanged(string);
		}
	}

	public String getText() {
		return this.text;
	}

	public String getSelectedText() {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		return this.text.substring(i, j);
	}

	@Override
	public void setX(int i) {
		super.setX(i);
		this.updateTextPosition();
	}

	@Override
	public void setY(int i) {
		super.setY(i);
		this.updateTextPosition();
	}

	public void write(String string) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = this.maxLength - this.text.length() - (i - j);
		if (k > 0) {
			String string2 = StringUtil.filterText(string);
			int l = string2.length();
			if (k < l) {
				if (Character.isHighSurrogate(string2.charAt(k - 1))) {
					k--;
				}

				string2 = string2.substring(0, k);
				l = k;
			}

			String string3 = new StringBuilder(this.text).replace(i, j, string2).toString();
			if (this.immediatePredicate.test(string3)) {
				this.text = string3;
				this.setSelectionStart(i + l);
				this.setSelectionEnd(this.selectionStart);
				this.onChanged(this.text);
			}
		}
	}

	private void onChanged(String string) {
		if (this.changedListener != null) {
			this.changedListener.accept(string);
		}

		this.updateTextPosition();
	}

	private void erase(int i, boolean bl) {
		if (bl) {
			this.eraseWords(i);
		} else {
			this.eraseCharacters(i);
		}
	}

	public void eraseWords(int i) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				this.eraseCharactersTo(this.getWordSkipPosition(i));
			}
		}
	}

	public void eraseCharacters(int i) {
		this.eraseCharactersTo(this.getCursorPosWithOffset(i));
	}

	public void eraseCharactersTo(int i) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				int j = Math.min(i, this.selectionStart);
				int k = Math.max(i, this.selectionStart);
				if (j != k) {
					String string = new StringBuilder(this.text).delete(j, k).toString();
					if (this.immediatePredicate.test(string)) {
						this.text = string;
						this.setCursor(j, false);
					}
				}
			}
		}
	}

	public int getWordSkipPosition(int i) {
		return this.getWordSkipPosition(i, this.getCursor());
	}

	private int getWordSkipPosition(int i, int j) {
		return this.getWordSkipPosition(i, j, true);
	}

	// despair
	private int getWordSkipPosition(int i, int j, boolean bl) {
		int k = j;
		boolean bl2 = i < 0;
		int l = Math.abs(i);

		for (int m = 0; m < l; m++) {
			if (!bl2) {
				int n = this.text.length();
				k = this.text.indexOf(32, k);
				if (k == -1) {
					k = n;
				} else {
					while (bl && k < n && this.text.charAt(k) == ' ') {
						k++;
					}
				}
			} else {
				while (bl && k > 0 && this.text.charAt(k - 1) == ' ') {
					k--;
				}

				while (k > 0 && this.text.charAt(k - 1) != ' ') {
					k--;
				}
			}
		}

		return k;
	}

	public void moveCursor(int i, boolean bl) {
		this.setCursor(this.getCursorPosWithOffset(i), bl);
	}

	private int getCursorPosWithOffset(int i) {
		return Util.offsetByCodepoints(this.text, this.selectionStart, i);
	}

	public void setCursor(int i, boolean bl) {
		this.setSelectionStart(i);
		if (!bl) {
			this.setSelectionEnd(this.selectionStart);
		}

		this.onChanged(this.text);
	}

	public void setSelectionStart(int i) {
		this.selectionStart = Mth.clamp(i, 0, this.text.length());
		this.updateFirstCharacterIndex(this.selectionStart);
	}

	public void setCursorToStart(boolean bl) {
		this.setCursor(0, bl);
	}

	public void setCursorToEnd(boolean bl) {
		this.setCursor(this.text.length(), bl);
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		if (this.isActive() && this.isFocused()) {
			switch (keyInput.key()) {
				// todo rename these to GLFW.KEY_...
				case 259:
					if (this.editable) {
						this.erase(-1, keyInput.hasControlDownWithQuirk());
					}

					return true;
				case 260:
				case 264:
				case 265:
				case 266:
				case 267:
				case 261:
					if (this.editable) {
						this.erase(1, keyInput.hasControlDownWithQuirk());
					}

					return true;
				case 262:
					if (keyInput.hasControlDownWithQuirk()) {
						this.setCursor(this.getWordSkipPosition(1), keyInput.hasShiftDown());
					} else {
						this.moveCursor(1, keyInput.hasShiftDown());
					}

					return true;
				case 263:
					if (keyInput.hasControlDownWithQuirk()) {
						this.setCursor(this.getWordSkipPosition(-1), keyInput.hasShiftDown());
					} else {
						this.moveCursor(-1, keyInput.hasShiftDown());
					}

					return true;
				case 268:
					this.setCursorToStart(keyInput.hasShiftDown());
					return true;
				case 269:
					this.setCursorToEnd(keyInput.hasShiftDown());
					return true;
				default:
					if (keyInput.isSelectAll()) {
						this.setCursorToEnd(false);
						this.setSelectionEnd(0);
						return true;
					} else if (keyInput.isCopy()) {
						Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
						return true;
					} else if (keyInput.isPaste()) {
						if (this.isEditable()) {
							this.write(Minecraft.getInstance().keyboardHandler.getClipboard());
						}

						return true;
					} else {
						if (keyInput.isCut()) {
							Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
							if (this.isEditable()) {
								this.write("");
							}

							return true;
						}

						return false;
					}
			}
		} else {
			return false;
		}
	}

	public boolean isActive() {
		//TODO: switching to mojmap somehow broke this??? I think i've fixed it but this needs to be looked into
		return active && this.isEditable();
	}

	@Override
	public boolean charTyped(CharacterEvent charInput) {
		if (!this.isActive()) {
			return false;
		} else if (charInput.isAllowedChatCharacter()) {
			if (this.editable) {
				this.write(charInput.codepointAsString());
			}

			return true;
		} else {
			return false;
		}
	}

	private int calculateCursorPos(MouseButtonEvent click) {
		int i = Math.min(Mth.floor(click.x()) - this.textX, this.getInnerWidth());
		String string = this.text.substring(this.firstCharacterIndex);
		return this.firstCharacterIndex + this.textRenderer.plainSubstrByWidth(string, i).length();
	}

	private void selectWord(MouseButtonEvent click) {
		int i = this.calculateCursorPos(click);
		int j = this.getWordSkipPosition(-1, i);
		int k = this.getWordSkipPosition(1, i);
		this.setCursor(j, false);
		this.setCursor(k, true);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		if (bl) {
			this.selectWord(click);
		} else {
			this.setCursor(this.calculateCursorPos(click), click.hasShiftDown());
		}
	}

	@Override
	protected void onDrag(MouseButtonEvent click, double d, double e) {
		this.setCursor(this.calculateCursorPos(click), true);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
		if (this.isVisible()) {
			int colorIShouldBe = this.editable ? this.editableColor : this.uneditableColor;
			int cursorOffset = this.selectionStart - this.firstCharacterIndex;
			String visibleText = this.textRenderer.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
			boolean cursorVisible = cursorOffset >= 0 && cursorOffset <= visibleText.length();

			long time = Util.getMillis();
			if (!cursorXTweener.isAtTarget()) {
				isFirstAfterAtTarget = true;
				time = this.lastSwitchFocusTime;
			} else if (isFirstAfterAtTarget) {
				isFirstAfterAtTarget = false;
				this.lastSwitchFocusTime = time;
			}
			boolean blink = this.isFocused() && (time - this.lastSwitchFocusTime) / 300L % 2L == 0L && cursorVisible;

			int actualTextX = this.textX;
			int visibleSelectedCharacters = Mth.clamp(this.selectionEnd - this.firstCharacterIndex, 0, visibleText.length());
			if (!visibleText.isEmpty()) {
				String string2 = cursorVisible ? visibleText.substring(0, cursorOffset) : visibleText;
				Component formatted = this.format(string2);
				context.drawString(this.textRenderer, this.format(visibleText), actualTextX, this.textY, colorIShouldBe, this.textShadow);
				actualTextX += this.textRenderer.width(formatted) + 1;
			}

			boolean shouldBePipe = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
			int cursorX = actualTextX;
			if (!cursorVisible) {
				cursorX = cursorOffset > 0 ? this.textX + this.width : this.textX;
			} else if (shouldBePipe) {
				cursorX = --actualTextX;
			}

//			if (!visibleText.isEmpty() && cursorVisible && cursorOffset < visibleText.length()) {
//				context.drawString(this.textRenderer, this.format(visibleText.substring(cursorOffset)), actualTextX, this.textY, colorIShouldBe, this.textShadow);
//			}

			if (this.placeholder != null && visibleText.isEmpty() && !this.isFocused()) {
				context.drawString(this.textRenderer, this.placeholder, actualTextX, this.textY, colorIShouldBe);
			}

			if (!shouldBePipe && this.suggestion != null) {
				context.drawString(this.textRenderer, this.suggestion, cursorX - 1, this.textY, -8355712, this.textShadow);
			}
			if (visibleSelectedCharacters != cursorOffset) {
				int p = this.textX + this.textRenderer.width(visibleText.substring(0, visibleSelectedCharacters));
//					context.drawSelection(
//							Math.min(o, this.getX() + this.width), this.textY - 1, Math.min(p - 1, this.getX() + this.width), this.textY + 1 + 9, this.invertSelectionBackground
//					);
				// todo: setTargetAndUpdate?
				targetSelectionX1 = Math.min(cursorX, this.getX() + this.width);
				targetSelectionX2 = Math.min(p - 1, this.getX() + this.width);
				selectionX1Tweener.update();
				selectionX2Tweener.update();
				if (this.firstAfterSelect) {
					selectionX1Tweener.snapToTarget();
					selectionX2Tweener.snapToTarget();
					this.firstAfterSelect = false;
				}
				var x1 = selectionX1Tweener.getF();
				var x2 = selectionX2Tweener.getF();
				if (x1 > x2) {
					var temp = x1;
					x1 = x2;
					x2 = temp;
				}
				if (this.invertSelectionBackground) {
					RenderUtils.fillEvil(context, x1, this.textY - 1, x2, this.textY + 1 + 9, -1, RenderPipelines.GUI_INVERT);
				}
				RenderUtils.fillEvil(context, x1, this.textY - 1, x2, this.textY + 1 + 9, -16776961, RenderPipelines.GUI_TEXT_HIGHLIGHT);
			} else {
				this.firstAfterSelect = true;
			}

			// do calculations regardless of if blink
			targetCursorX = cursorX;
			targetCursorY = shouldBePipe ? this.textY - 1 : this.textY + 8;
			targetCursorWidth = shouldBePipe ? 1 : 5;
			targetCursorHeight = shouldBePipe ? 10 : 1;
			cursorXTweener.update();
			cursorYTweener.update();
			cursorHeightTweener.update();
			cursorWidthTweener.update();

			if (justFocused) {
				cursorXTweener.snapToTarget();
				cursorYTweener.snapToTarget();
				cursorHeightTweener.snapToTarget();
				cursorWidthTweener.snapToTarget();
				justFocused = false;
			}

			if (blink) {
				float x1 = cursorXTweener.getF();
				float x2 = cursorXTweener.getF() + cursorWidthTweener.getF();
				float y1 = cursorYTweener.getF();
				float y2 = cursorYTweener.getF() + cursorHeightTweener.getF();
				RenderUtils.fillEvil(context, x1, y1, x2, y2, colorIShouldBe);
			}

			if (this.isHovered()) {
				context.requestCursor(this.isEditable() ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED);
			}
		}
	}

	private Component format(String string) {
		return this.formatter.apply(string);
	}

	public void setFormatter(Function<String, Component> formatter) {
		this.formatter = formatter;
	}

	private void updateTextPosition() {
		if (this.textRenderer != null) {
			String string = this.textRenderer.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
			this.textX = this.getX() + (this.isCentered() ? (this.getWidth() - this.textRenderer.width(string)) / 2 : (this.drawsBackground ? 4 : 0));
			this.textY = this.drawsBackground ? this.getY() + (this.height - 8) / 2 : this.getY();
		}
	}

	public void setMaxLength(int i) {
		this.maxLength = i;
		if (this.text.length() > i) {
			this.text = this.text.substring(0, i);
			this.onChanged(this.text);
		}
	}

	private int getMaxLength() {
		return this.maxLength;
	}

	public int getCursor() {
		return this.selectionStart;
	}

	public boolean drawsBackground() {
		return this.drawsBackground;
	}

	public void setDrawsBackground(boolean bl) {
		this.drawsBackground = bl;
		this.updateTextPosition();
	}

	public void setEditableColor(int i) {
		this.editableColor = i;
	}

	public void setUneditableColor(int i) {
		this.uneditableColor = i;
	}


	private boolean isEditable() {
		return this.editable;
	}

	public void setEditable(boolean bl) {
		this.editable = bl;
	}

	private boolean isCentered() {
		return this.centered;
	}

	public void setCentered(boolean bl) {
		this.centered = bl;
		this.updateTextPosition();
	}

	@Override
	public void setFocused(boolean bl) {
		if (!bl) {
			this.setSelectionEnd(0);
			this.setSelectionStart(0);
			if (this.predicate.test(this.getText())) {
				if (this.changedListener != null) {
					this.changedListener.accept(this.getText());
				}
				this.lastAccepted = this.getText();
			} else {
				this.setText(lastAccepted);
			}
		}
		if (this.focusUnlocked || bl) {
			super.setFocused(bl);
			if (bl) {
				this.lastSwitchFocusTime = Util.getMillis();
				this.justFocused = true;
			}
		}
	}

	public void setTextShadow(boolean bl) {
		this.textShadow = bl;
	}

	public void setInvertSelectionBackground(boolean bl) {
		this.invertSelectionBackground = bl;
	}

	public int getInnerWidth() {
		return this.drawsBackground() ? this.width - 8 : this.width;
	}

	public void setSelectionEnd(int i) {
		this.selectionEnd = Mth.clamp(i, 0, this.text.length());
		this.updateFirstCharacterIndex(this.selectionEnd);
	}

	private void updateFirstCharacterIndex(int i) {
		if (this.textRenderer != null) {
			this.firstCharacterIndex = Math.min(this.firstCharacterIndex, this.text.length());
			int j = this.getInnerWidth();
			String string = this.textRenderer.plainSubstrByWidth(this.text.substring(this.firstCharacterIndex), j);
			int k = string.length() + this.firstCharacterIndex;
			if (i == this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex - this.textRenderer.plainSubstrByWidth(this.text, j, true).length();
			}

			if (i > k) {
				this.firstCharacterIndex += i - k;
			} else if (i <= this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex - (this.firstCharacterIndex - i);
			}

			this.firstCharacterIndex = Mth.clamp(this.firstCharacterIndex, 0, this.text.length());
		}
	}

	public void setFocusUnlocked(boolean bl) {
		this.focusUnlocked = bl;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean bl) {
		this.visible = bl;
	}

	public void setSuggestion(@Nullable String string) {
		this.suggestion = string;
	}

	public int getCharacterX(int i) {
		return i > this.text.length() ? this.getX() : this.getX() + this.textRenderer.width(this.text.substring(0, i));
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput narrationMessageBuilder) {
		narrationMessageBuilder.add(NarratedElementType.TITLE, this.createNarrationMessage());
	}

	public void setPlaceholder(Component text) {
		boolean bl = text.getStyle().equals(Style.EMPTY);
		this.placeholder = bl ? text.copy().withStyle(PLACEHOLDER_STYLE) : text;
	}
}
