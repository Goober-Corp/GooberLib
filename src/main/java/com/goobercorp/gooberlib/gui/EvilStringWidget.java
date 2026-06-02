package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringWidget extends EvilBaseWidget {
	public static final Style PLACEHOLDER_STYLE = Style.EMPTY.withColor(Formatting.DARK_GRAY);
	private final TextRenderer textRenderer;
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
	private Predicate<String> textPredicate = Objects::nonNull;
	@Nullable
	private Text placeholder;
	private long lastSwitchFocusTime = Util.getMeasuringTimeMs();
	private int textX;
	private int textY;
	private final Predicate<String> predicate;
	private String lastAccepted;
	private int targetCursorX = this.getX();
	private int targetCursorWidth;
	private int targetCursorHeight;
	private int targetSelectionX1;
	private int targetSelectionX2;
	// todo: change tweener with .setTarget or something? and .setValue to set the value and not have tweening
	private final Tweener cursorPosTweener = new Tweener(() -> targetCursorX, 10);
	private final Tweener cursorWidthTweener = new Tweener(() -> targetCursorWidth, 10);
	private final Tweener cursorHeightTweener = new Tweener(() -> targetCursorHeight, 10);
	private final Tweener selectionX1Tweener = new Tweener(() -> targetSelectionX1, 10);
	private final Tweener selectionX2Tweener = new Tweener(() -> targetSelectionX2, 10);
	private boolean isFirstAfterAtTarget = false;
	private boolean firstAfterSelect = true;

	public EvilStringWidget(int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, String initial) {
		super(Text.empty(), x, y, width, height);
		this.textRenderer = MinecraftClient.getInstance().textRenderer;
		this.lastAccepted = initial;
		this.setText(initial);
		this.setChangedListener(changedListener);
		this.predicate = predicate;
	}

	public EvilStringWidget(int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, String initial, int colorOverride) {
		this(x, y, width, height, changedListener, predicate, initial);
		//TODO: automatically calculate good selection color. blue text here means it's just white when selected.
		this.editableColor = colorOverride;
	}

	public void setChangedListener(Consumer<String> consumer) {
		this.changedListener = consumer;
	}

	@Override
	protected MutableText getNarrationMessage() {
		Text text = this.getMessage();
		return Text.translatable("gui.narrate.editBox", text, this.text);
	}

	public void setText(String string) {
		if (this.textPredicate.test(string)) {
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

	public void setTextPredicate(Predicate<String> predicate) {
		this.textPredicate = predicate;
	}

	public void write(String string) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = this.maxLength - this.text.length() - (i - j);
		if (k > 0) {
			String string2 = StringHelper.stripInvalidChars(string);
			int l = string2.length();
			if (k < l) {
				if (Character.isHighSurrogate(string2.charAt(k - 1))) {
					k--;
				}

				string2 = string2.substring(0, k);
				l = k;
			}

			String string3 = new StringBuilder(this.text).replace(i, j, string2).toString();
			if (this.textPredicate.test(string3)) {
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
					if (this.textPredicate.test(string)) {
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
		return Util.moveCursor(this.text, this.selectionStart, i);
	}

	public void setCursor(int i, boolean bl) {
		this.setSelectionStart(i);
		if (!bl) {
			this.setSelectionEnd(this.selectionStart);
		}

		this.onChanged(this.text);
	}

	public void setSelectionStart(int i) {
		this.selectionStart = MathHelper.clamp(i, 0, this.text.length());
		this.updateFirstCharacterIndex(this.selectionStart);
	}

	public void setCursorToStart(boolean bl) {
		this.setCursor(0, bl);
	}

	public void setCursorToEnd(boolean bl) {
		this.setCursor(this.text.length(), bl);
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		if (this.isInteractable() && this.isFocused()) {
			switch (keyInput.comp_4795()) {
				// todo rename these to GLFW.KEY_...
				case 259:
					if (this.editable) {
						this.erase(-1, keyInput.hasCtrlOrCmd());
					}

					return true;
				case 260:
				case 264:
				case 265:
				case 266:
				case 267:
				case 261:
					if (this.editable) {
						this.erase(1, keyInput.hasCtrlOrCmd());
					}

					return true;
				case 262:
					if (keyInput.hasCtrlOrCmd()) {
						this.setCursor(this.getWordSkipPosition(1), keyInput.hasShift());
					} else {
						this.moveCursor(1, keyInput.hasShift());
					}

					return true;
				case 263:
					if (keyInput.hasCtrlOrCmd()) {
						this.setCursor(this.getWordSkipPosition(-1), keyInput.hasShift());
					} else {
						this.moveCursor(-1, keyInput.hasShift());
					}

					return true;
				case 268:
					this.setCursorToStart(keyInput.hasShift());
					return true;
				case 269:
					this.setCursorToEnd(keyInput.hasShift());
					return true;
				default:
					if (keyInput.isSelectAll()) {
						this.setCursorToEnd(false);
						this.setSelectionEnd(0);
						return true;
					} else if (keyInput.isCopy()) {
						MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
						return true;
					} else if (keyInput.isPaste()) {
						if (this.isEditable()) {
							this.write(MinecraftClient.getInstance().keyboard.getClipboard());
						}

						return true;
					} else {
						if (keyInput.isCut()) {
							MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
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
		return this.isInteractable() && this.isFocused() && this.isEditable();
	}

	@Override
	public boolean charTyped(CharInput charInput) {
		if (!this.isActive()) {
			return false;
		} else if (charInput.isValidChar()) {
			if (this.editable) {
				this.write(charInput.asString());
			}

			return true;
		} else {
			return false;
		}
	}

	private int calculateCursorPos(Click click) {
		int i = Math.min(MathHelper.floor(click.comp_4798()) - this.textX, this.getInnerWidth());
		String string = this.text.substring(this.firstCharacterIndex);
		return this.firstCharacterIndex + this.textRenderer.trimToWidth(string, i).length();
	}

	private void selectWord(Click click) {
		int i = this.calculateCursorPos(click);
		int j = this.getWordSkipPosition(-1, i);
		int k = this.getWordSkipPosition(1, i);
		this.setCursor(j, false);
		this.setCursor(k, true);
	}

	@Override
	public void onClick(Click click, boolean bl) {
		if (bl) {
			this.selectWord(click);
		} else {
			this.setCursor(this.calculateCursorPos(click), click.hasShift());
		}
	}

	@Override
	protected void onDrag(Click click, double d, double e) {
		this.setCursor(this.calculateCursorPos(click), true);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	public void renderWidget(DrawContext context, double mouseX, double mouseY, float delta) {
		if (this.isVisible()) {
			int k = this.editable ? this.editableColor : this.uneditableColor;
			int l = this.selectionStart - this.firstCharacterIndex;
			String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
			boolean bl = l >= 0 && l <= string.length();

			long measuringTimeMs = Util.getMeasuringTimeMs();
			if (!cursorPosTweener.isAtTarget()) {
				isFirstAfterAtTarget = true;
				measuringTimeMs = this.lastSwitchFocusTime;
			} else if (isFirstAfterAtTarget) {
				isFirstAfterAtTarget = false;
				this.lastSwitchFocusTime = Util.getMeasuringTimeMs();
			}
			boolean bl2 = this.isFocused() && (measuringTimeMs - this.lastSwitchFocusTime) / 300L % 2L == 0L && bl;
			int m = this.textX;
			int n = MathHelper.clamp(this.selectionEnd - this.firstCharacterIndex, 0, string.length());
			if (!string.isEmpty()) {
				String string2 = bl ? string.substring(0, l) : string;
				OrderedText orderedText = this.format(string2);
				context.drawText(this.textRenderer, orderedText, m, this.textY, k, this.textShadow);
				m += this.textRenderer.getWidth(orderedText) + 1;
			}

			boolean bl3 = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
			int o = m;
			if (!bl) {
				o = l > 0 ? this.textX + this.width : this.textX;
			} else if (bl3) {
				o = m - 1;
				m--;
			}

			if (!string.isEmpty() && bl && l < string.length()) {
				context.drawText(this.textRenderer, this.format(string.substring(l)), m, this.textY, k, this.textShadow);
			}

			if (this.placeholder != null && string.isEmpty() && !this.isFocused()) {
				context.drawTextWithShadow(this.textRenderer, this.placeholder, m, this.textY, k);
			}

			if (!bl3 && this.suggestion != null) {
				context.drawText(this.textRenderer, this.suggestion, o - 1, this.textY, -8355712, this.textShadow);
			}
			if (n != l) {
				int p = this.textX + this.textRenderer.getWidth(string.substring(0, n));
//					context.drawSelection(
//							Math.min(o, this.getX() + this.width), this.textY - 1, Math.min(p - 1, this.getX() + this.width), this.textY + 1 + 9, this.invertSelectionBackground
//					);
				// todo: setTargetAndUpdate?
				targetSelectionX1 = Math.min(o, this.getX() + this.width);
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

			if (bl2) {
				targetCursorX = o;
				targetCursorWidth = bl3 ? 1 : 5;
				//This one is range 0-1  because like. it goes in both directions ???
				targetCursorHeight = bl3 ? 1 : 0;
				cursorPosTweener.update();
				cursorHeightTweener.update();
				cursorWidthTweener.update();
				RenderUtils.fillEvil(context, cursorPosTweener.getF(), MathHelper.lerp(cursorHeightTweener.getF(), (this.textY + 7), (this.textY - 1)), (cursorPosTweener.getF() + cursorWidthTweener.getF()), MathHelper.lerp(cursorHeightTweener.getF(), this.textY + 8, this.textY + 1 + 9), k);
			}

			if (this.isHovered()) {
				context.setCursor(this.isEditable() ? StandardCursors.IBEAM : StandardCursors.NOT_ALLOWED);
			}
		}
	}

	private OrderedText format(String string) {
		return OrderedText.styledForwardsVisitedString(string, Style.EMPTY);
	}

	private void updateTextPosition() {
		if (this.textRenderer != null) {
			String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
			this.textX = this.getX() + (this.isCentered() ? (this.getWidth() - this.textRenderer.getWidth(string)) / 2 : (this.drawsBackground ? 4 : 0));
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
				this.lastSwitchFocusTime = Util.getMeasuringTimeMs();
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
		this.selectionEnd = MathHelper.clamp(i, 0, this.text.length());
		this.updateFirstCharacterIndex(this.selectionEnd);
	}

	private void updateFirstCharacterIndex(int i) {
		if (this.textRenderer != null) {
			this.firstCharacterIndex = Math.min(this.firstCharacterIndex, this.text.length());
			int j = this.getInnerWidth();
			String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), j);
			int k = string.length() + this.firstCharacterIndex;
			if (i == this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex - this.textRenderer.trimToWidth(this.text, j, true).length();
			}

			if (i > k) {
				this.firstCharacterIndex += i - k;
			} else if (i <= this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex - (this.firstCharacterIndex - i);
			}

			this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, this.text.length());
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
		return i > this.text.length() ? this.getX() : this.getX() + this.textRenderer.getWidth(this.text.substring(0, i));
	}

	@Override
	public void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {
		narrationMessageBuilder.put(NarrationPart.TITLE, this.getNarrationMessage());
	}

	public void setPlaceholder(Text text) {
		boolean bl = text.getStyle().equals(Style.EMPTY);
		this.placeholder = bl ? text.copy().fillStyle(PLACEHOLDER_STYLE) : text;
	}
}
