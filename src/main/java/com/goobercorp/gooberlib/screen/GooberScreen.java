package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.nav.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.ScrollTweener;
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

import java.util.ArrayList;
import java.util.List;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class GooberScreen extends Screen {
	public static final int VERTICAL_PADDING = 32;
	public static final int CHILD_INSET = 16;
	private final BuiltConfig config;
	private final Screen parent;
	private Component descriptionText = Component.literal("");
	private float descriptionAnimationProgress = 0;
	private float categoryHoverProgress = 1;
	private float screenCategoryAnimationState = 0;
	private EvilTabNavigationWidget tabNavigationWidget;
	private int tabHoldTicks = 20;
	private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
	private double scrollProgress = 0;
	private final ScrollTweener scrollTweener = new ScrollTweener(() -> scrollProgress, writeTo -> scrollProgress = writeTo, -1000, 0);
	private int lastScrollTicks = 0;
	private final Tweener categoryTweener = new Tweener(() -> screenCategoryAnimationState);
	private final Tab[] tabs;
	private boolean animateHoverDescription = false;
	private final boolean showTabs;

	private final String modId;
	private final List<PrecisePositionWidgetWrapper<CategoryWidget>> categoryWidgets = new ArrayList<>();

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

	private void setWidgetOffsets() {
		for (PrecisePositionWidgetWrapper<?> entry : categoryWidgets) {
			//TODO: sticky groups
			entry.setOffsetY(scrollTweener.get());
			entry.setOffsetX(-width * categoryTweener.get());
		}
	}

	@Override
	public void renderBackground(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
		//think of this as the pre-render
		updateTweeners();
		setWidgetOffsets();
		double mX, mY;
		mX = Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
		mY = Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());
		setHoverText(mX, mY);
		//Cursor glow
		RenderUtils.fillEvil(drawContext, (float) mX, (float) mY, (float) (mX + 2.5f), (float) (mY + 2.5F), MainConfig.primaryCol);
		newMatrixScope(drawContext, stack -> {
			stack.translate((float) (-mX * 0.1F), (float) (-mY * 0.1F));
			stack.translate(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - 100, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2F - 100);
			stack.scale(2.5F, 2.5F);
			drawContext.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
		});


		drawCommon(drawContext, mouseX, mouseY, tickDelta);

		if (this.showTabs) {
			newMatrixScope(drawContext, stack -> {
				stack.translate(0, -26 * categoryHoverProgress);
				tabNavigationWidget.renderForBackgroundLayer(drawContext);
			});
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
//		drawVerticalLine(drawContext, CHILD_INSET, 0, height, -1);
//		drawVerticalLine(drawContext, width / 2F + CHILD_INSET, 0, height, -1);
//		drawVerticalLine(drawContext, width / 2F - CHILD_INSET, 0, height, -1);
//		drawVerticalLine(drawContext, width - CHILD_INSET, 0, height, -1);

		if (this.showTabs) {
			if (tabNavigationWidget.isMouseOver(mX, mY)) {
				tabHoldTicks = 10;
			}

			newMatrixScope(drawContext, matrix3x2fStack -> {
				matrix3x2fStack.translate(0, -26 * categoryHoverProgress);
				tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
			});

			// TODO: maybe store scroll height for each category?
			var catHeight = -getCurrentCategoryWidget().getWrapped().getHeight() + this.height;
			if (catHeight > height) {
				catHeight -= (height - VERTICAL_PADDING);
			}
			scrollTweener.min = catHeight;
		}
	}

	private PrecisePositionWidgetWrapper<CategoryWidget> getCurrentCategoryWidget() {
		return categoryWidgets.get(showTabs ? tabNavigationWidget.getCurrentTabIndex() : 0);
	}

	private void setHoverText(double mouseX, double mouseY) {
		Component hoverMessage = getCurrentCategoryWidget().getHoverMessage(mouseX, mouseY);
		if (hoverMessage != null && !hoverMessage.isEmpty()) {
			animateHoverDescription = true;
			this.descriptionText = hoverMessage;
		} else {
			animateHoverDescription = false;
		}
	}

	private List<PrecisePositionWidgetWrapper<?>> getAllWidgets() {
		List<PrecisePositionWidgetWrapper<?>> list = new ArrayList<>();

		for (PrecisePositionWidgetWrapper<CategoryWidget> pw : categoryWidgets) {
			CategoryWidget categoryWidget = pw.getWrapped();
			for (var child : categoryWidget.children()) {
				if (child instanceof PrecisePositionWidgetWrapper<?> p) {
					var wrapped = p.getWrapped();
					if (wrapped instanceof SectionWidget section) {
						for (var sectionChild : section.children()) {
							if (sectionChild instanceof PrecisePositionWidgetWrapper<?> ppww) {
								list.add(ppww);
							}
						}
					}
					list.add(p);
				}
			}
		}

		return list;
	}

	private void drawCommon(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta) {
		for (PrecisePositionWidgetWrapper<CategoryWidget> categoryWidget : categoryWidgets) {
			categoryWidget.render(drawContext, mouseX, mouseY, tickDelta);
		}
		drawHoveredDescription(drawContext);
	}

	private void drawHoveredDescription(GuiGraphics drawContext) {
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
		return super.keyPressed(keyInput);
	}

	private void updateTweeners() {
		this.scrollTweener.update();
		this.categoryTweener.update();
		//TODO: convert to tweeners
		categoryHoverProgress = (float) ease(categoryHoverProgress, tabHoldTicks > 0 ? 0 : 1, 15);
		int yeah = showTabs ? tabNavigationWidget.getCurrentTabIndex() : 0;
		screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);
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
			if ((!showTabs || !tabNavigationWidget.isMouseOver(click.x(), click.y())) && click.button() == 0) {
				if ((scrollProgress < scrollTweener.min) || (scrollProgress > scrollTweener.max)) {
					scrollProgress += deltaY * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
				} else {
					scrollProgress += deltaY;
				}
			}
		}
		return yeah;
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		boolean yeah = super.mouseScrolled(d, e, f, g);
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
