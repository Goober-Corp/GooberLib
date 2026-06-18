package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.nav.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.ScrollTweener;
import com.goobercorp.gooberlib.util.TargetedTweener;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.goobercorp.gooberlib.util.RenderUtils.*;

public class GooberScreen extends Screen {
	public static final int VERTICAL_PADDING = 32;
	public static final int CHILD_INSET = 16;
	protected final BuiltConfig config;
	protected final Screen parent;
	protected Component descriptionText = Component.literal("");
	protected float descriptionAnimationProgress = 0;
	protected float categoryHoverProgress = 1;
	protected float screenCategoryAnimationState = 0;
	protected EvilTabNavigationWidget tabNavigationWidget;
	protected int tabHoldTicks = 20;
	protected final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
	protected double scrollProgress = 0;
	protected final ScrollTweener scrollTweener = new ScrollTweener(() -> scrollProgress, writeTo -> scrollProgress = writeTo, -1000, 0);
	protected int lastScrollTicks = 0;
	protected final Tweener categoryTweener = new Tweener(() -> screenCategoryAnimationState);
	protected final Tab[] tabs;
	protected boolean animateHoverDescription = false;
	protected boolean showTabs;
	public TargetedTweener mouseXTweener = new TargetedTweener(20);
	public TargetedTweener mouseYTweener = new TargetedTweener(20);

	protected final String modId;
	protected final List<PrecisePositionWidgetWrapper<CategoryWidget>> categoryWidgets = new ArrayList<>();

	public GooberScreen(BuiltConfig config, Screen parent, String modId) {
		super(config.title());
		this.config = config;
		this.parent = parent;
		this.modId = modId;

		this.tabs = new GridLayoutTab[config.categories().size()];
		for (int i = 0; i < config.categories().size(); i++) {
			tabs[i] = new GridLayoutTab(config.categories().get(i).metadata().name());
		}
		showTabs = tabs.length > 1;
	}

	@Override
	protected void init() {
		categoryWidgets.clear();
		if (this.showTabs) {
			this.tabNavigationWidget = this.addWidget(EvilTabNavigationWidget.builder(tabManager, width).tabs(tabs).build());
			this.tabNavigationWidget.init();
			tabNavigationWidget.selectTab(0, false);
		}

		for (ConfigCategory c : config.categories()) {
			int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() * (config.categories().indexOf(c));
			var cat = new CategoryWidget(c, 0, 0, width, height);
			PrecisePositionWidgetWrapper<CategoryWidget> pw = new PrecisePositionWidgetWrapper<>(cat, x, VERTICAL_PADDING, () -> c.metadata().description());
			this.addWidget(pw);
			categoryWidgets.add(pw);
		}
		//to prevent weirdness on resize
		setWidgetOffsets();
	}

	protected void setWidgetOffsets() {
		for (PrecisePositionWidgetWrapper<?> entry : categoryWidgets) {
			//TODO: sticky groups
			entry.setOffsetY(scrollTweener.get());
			entry.setOffsetX(-width * categoryTweener.get());
		}
	}

	public static int getRainbow(float delay, float speed, float saturation, float brightness) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + (int) (delay * 1000))) * speed / 20;
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0f), saturation, brightness).getRGB();
	}

	@Override
	public void renderBackground(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
		if (MainConfig.WOKE.getValue()) {
			MainConfig.primaryCol = (0xFF << 24) | getRainbow(0, 1, MainConfig.WOKE_STRENGTH.value, 1);
			MainConfig.shadowCol = (0xFF << 24) | getRainbow(0, 1, MainConfig.WOKE_STRENGTH.value, 0.25F);
		}
		//think of this as the pre-render
		double mX, mY;
		mX = Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
		mY = Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());
		//TODO: maybe move this to the tweener?
		if (!(Double.isNaN(mX) || Double.isInfinite(mX) || Double.isNaN(mY) || Double.isInfinite(mY))) {
			mouseXTweener.setTarget(mX);
			mouseYTweener.setTarget(mY);
		}
		updateTweeners();
		setWidgetOffsets();
		setHoverText(mX, mY);
		newMatrixScope(drawContext, stack -> {
			stack.translate((float) (-mouseXTweener.get() * 0.1F), (float) (-mouseYTweener.get() * 0.1F));
			stack.translate(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - 100, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2F - 100);
			stack.scale(2.5F, 2.5F);
			drawContext.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
		});
		if (MainConfig.BACKGROUND_GLOW.value) {
			if (Minecraft.getInstance().options.getMenuBackgroundBlurriness() > 0) {
				RenderUtils.fillEvil(drawContext, (float) mX, (float) mY, (float) (mX + 2.5f), (float) (mY + 2.5F), MainConfig.primaryCol);
			}
			drawCommon(drawContext, mouseX, mouseY, tickDelta);
			if (this.showTabs) {
				newMatrixScope(drawContext, stack -> {
					stack.translate(0, -26 * categoryHoverProgress);
					tabNavigationWidget.renderForBackgroundLayer(drawContext);
				});
			}
		}

		if (this == Minecraft.getInstance().screen) {
			drawContext.nextStratum();
			super.renderBackground(drawContext, mouseX, mouseY, tickDelta);
		}
	}

	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
		double mX, mY;
		mX = Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
		mY = Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());
		drawCommon(drawContext, mouseX, mouseY, tickDelta);
		if (MainConfig.DEBUG_GUIDELINES.value) {
			drawVerticalLine(drawContext, CHILD_INSET / 2F, 0, height, -1);
			drawVerticalLine(drawContext, width / 2F + CHILD_INSET / 2F, 0, height, -1);
			drawVerticalLine(drawContext, width / 2F - CHILD_INSET / 2F, 0, height, -1);
			drawVerticalLine(drawContext, width - CHILD_INSET / 2F, 0, height, -1);
		}

		if (this.showTabs) {
			if (tabNavigationWidget.isMouseOver(mX, mY)) {
				tabHoldTicks = 10;
			}
			if (!MainConfig.HIDE_TABS.getValue()) {
				tabHoldTicks = 10;
			}

			newMatrixScope(drawContext, matrix3x2fStack -> {
				matrix3x2fStack.translate(0, -26 * categoryHoverProgress);
				tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
			});
		}
		//TODO: this still breaks
		var catHeight = getCurrentCategoryWidget().getWrapped().getHeight();
		if (catHeight < height) {
			catHeight += (height - VERTICAL_PADDING);
		}
		catHeight -= height;
		catHeight += VERTICAL_PADDING;

		scrollTweener.min = -catHeight;
	}

	protected PrecisePositionWidgetWrapper<CategoryWidget> getCurrentCategoryWidget() {
		return categoryWidgets.get(showTabs ? tabNavigationWidget.getCurrentTabIndex() : 0);
	}

	protected void setHoverText(double mouseX, double mouseY) {
		Component hoverMessage = getCurrentCategoryWidget().getHoverMessage(mouseX, mouseY);
		if (hoverMessage != null && !hoverMessage.isEmpty()) {
			animateHoverDescription = true;
			this.descriptionText = hoverMessage;
		} else {
			animateHoverDescription = false;
		}
	}

	protected void drawCommon(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
		for (PrecisePositionWidgetWrapper<CategoryWidget> categoryWidget : categoryWidgets) {
			categoryWidget.render(drawContext, mouseX, mouseY, tickDelta);
		}
		drawHoveredDescription(drawContext);
	}

	protected void drawHoveredDescription(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			List<FormattedCharSequence> lines = font.split(descriptionText, width);
			int linesHeight = lines.size() * 9;
			stack.translate((float) (drawContext.guiWidth() / 2), this.height - linesHeight * descriptionAnimationProgress);
//			RenderUtils.fillEvil(drawContext, 0, -1, width / 2F, linesHeight, MainConfig.shadowCol, 0x00000000);
//			RenderUtils.fillEvil(drawContext, -width / 2F, -1, 0, linesHeight, 0x00000000, MainConfig.shadowCol);
			RenderUtils.fillEvil(drawContext, -width / 2F, -1, width / 2F, linesHeight, 0, MainConfig.shadowCol, MainConfig.shadowCol, 0);
			for (FormattedCharSequence orderedText : lines) {
				drawContext.drawCenteredString(font, orderedText, 0, 9 * lines.indexOf(orderedText), MainConfig.primaryCol);
			}
//			drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, 0, 0, MainConfig.primaryCol);
		});
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		if (this.getFocused() != null && this.getFocused().keyPressed(keyInput)) {
			return true;
		} else {
			return super.keyPressed(keyInput);
		}
	}

	protected void updateTweeners() {
		this.scrollTweener.update();
		mouseXTweener.update();
		mouseYTweener.update();
		//TODO: convert to tweeners
		categoryHoverProgress = (float) ease(categoryHoverProgress, tabHoldTicks > 0 ? 0 : 1, 15);
		screenCategoryAnimationState = showTabs ? tabNavigationWidget.getCurrentTabIndex() : 0;
		if (MainConfig.CATEGORY_ANIMATIONS.value) {
			this.categoryTweener.update();
		} else {
			categoryTweener.snapToTarget();
		}
		descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animateHoverDescription ? 1 : 0, 20);
	}

	@Override
	public void tick() {
		if (this.showTabs) {
			tabNavigationWidget.tick();
		}
		tabHoldTicks = Math.clamp(tabHoldTicks - 1, 0, 100);
		lastScrollTicks++;
		if (lastScrollTicks > 2) {
			scrollTweener.setInteractionState(false);
		}
	}

	@Override
	public void onClose() {
		setFocused(null);
		Minecraft.getInstance().setScreen(parent);
		GooberLibApi.save(modId, config);
	}

	@Override
	public void resize(int i, int j) {
		int selectedTab = 0;
		if (this.showTabs) {
			selectedTab = tabNavigationWidget.getCurrentTabIndex();
		}
		super.resize(i, j);
		if (this.showTabs) {
			tabNavigationWidget.selectTab(selectedTab == -1 ? 0 : selectedTab, false);
		}
		setFocused(null);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		boolean b = super.mouseClicked(click, bl);
		if (!b) {
			setFocused(null);
		}
		return b;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
		if (this.isDragging()) {
			this.setDragging(false);
		}
		if (this.getFocused() != null) {
			return this.getFocused().mouseReleased(mouseButtonEvent);
		}
		return false;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent click, double deltaX, double deltaY) {
		boolean yeah = super.mouseDragged(click, deltaX, deltaY);
		if (!yeah) {
			lastScrollTicks = 0;
			scrollTweener.setInteractionState(true);
			if ((!showTabs || !tabNavigationWidget.isMouseOver(click.x(), click.y()))) {
				if ((scrollProgress < scrollTweener.min) || (scrollProgress > scrollTweener.max)) {
					if (click.button() == 0 || click.button() == 2) {
						scrollProgress += deltaY * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
					}
				} else {
					if (click.button() == 0) {
						scrollProgress += deltaY;
					} else if (click.button() == 2) {
						scrollProgress += deltaY * 5;
					}
				}
			}
		}
		return yeah;
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		boolean yeah;
		if (scrollTweener.isBeingInteractedWith) {
			yeah = false;
		} else {
			yeah = super.mouseScrolled(d, e, f, g);
		}
		if (!yeah) {
			lastScrollTicks = 0;
			scrollTweener.setInteractionState(true);
			if (!showTabs || !tabNavigationWidget.isMouseOver(d, e)) {
				if ((scrollProgress < scrollTweener.min && g < 0) || (scrollProgress > scrollTweener.max && g > 0)) {
					scrollProgress += g * 20 * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
				} else {
					scrollProgress += g * 20;
				}
			}
		}
		return yeah;
	}
}
