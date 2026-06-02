package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.gui.GroupTextWidget;
import com.goobercorp.gooberlib.gui.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.ScrollTweener;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class GooberScreen extends Screen {
	private static final int VERTICAL_PADDING = 32;
	private static final int CHILD_INSET = 16;
	private final BuiltConfig config;
	private final Screen parent;
	private Text descriptionText = Text.of("");
	private float descriptionAnimationProgress = 0;
	private float categoryHoverProgress = 1;
	private float screenCategoryAnimationState = 0;
	private EvilTabNavigationWidget tabNavigationWidget;
	private int tabHoldTicks = 20;
	private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
	private double scrollProgress = 0;
	private final ScrollTweener scrollTweener = new ScrollTweener(() -> scrollProgress, writeTo -> scrollProgress = writeTo, -1000, 0);
	private int lastScrollTicks = 0;
	private final Tweener categoryTweener = new Tweener(() -> screenCategoryAnimationState);
	private final HashMap<OptionHolder, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	private final Tab[] tabs;
	private final int[] heights;
	private boolean animateHoverDescription = false;
	public ScreenRect badbadbad = new ScreenRect(0, 0, 0, 0);

	private final String modId;

	public GooberScreen(BuiltConfig config, Screen parent, String modId) {
		super(config.title());
		this.config = config;
		this.parent = parent;
		this.modId = modId;

		this.tabs = new GridScreenTab[config.categories().size()];
		for (int i = 0; i < config.categories().size(); i++) {
			tabs[i] = new GridScreenTab(config.categories().get(i).metadata().name());
		}
		heights = new int[tabs.length];
	}

	@Override
	protected void init() {
		evilLayout.clear();
		if (this.tabs.length > 1) {
			this.tabNavigationWidget = this.addSelectableChild(EvilTabNavigationWidget.builder(tabManager, width).tabs(tabs).build());
			this.tabNavigationWidget.init();
			tabNavigationWidget.selectTab(0, false);
		}

		for (ConfigCategory c : config.categories()) {
			int x = MinecraftClient.getInstance().getWindow().getScaledWidth() * (config.categories().indexOf(c));
			int y = VERTICAL_PADDING;
			for (OptionHolder o : c.elements()) {
				if (o instanceof ConfigSection(
						Metadata metadata, List<OptionContext<?>> childOptionContexts
				)) {
					GroupTextWidget t = new GroupTextWidget(metadata.name(), textRenderer);
					PrecisePositionWidgetWrapper<GroupTextWidget> widgetWrapper = new PrecisePositionWidgetWrapper<>(t, x + ((double) MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - (double) textRenderer.getWidth(metadata.name()) / 2, y, metadata::description);
					evilLayout.put(o, widgetWrapper);
					if (new ScreenRect((int) widgetWrapper.getRealX(), (int) widgetWrapper.getRealY(), widgetWrapper.getWrapped().getRight(), widgetWrapper.getWrapped().getBottom()).overlaps(new ScreenRect(0, 0, width, height))) {
						t.renderProgress = 1;
					}
					addDrawableChild(widgetWrapper);
					y += VERTICAL_PADDING;
					for (OptionContext<?> yeah : childOptionContexts) {
						y += addOptionWithChildren(yeah, y, x + CHILD_INSET);
					}
				} else {
					y += addOptionWithChildren((OptionContext<?>) o, y, x + 5);
				}
				// TODO: maybe store scroll height for each category?
				heights[config.categories().indexOf(c)] = Math.max(y - height, 0);
			}
		}
		//to prevent weirdness on resize
		setWidgetOffsets();
	}

	private void setWidgetOffsets() {
		for (PrecisePositionWidgetWrapper<?> entry : evilLayout.values()) {

//            if (scrollTweener.get() < scrollTweener.min) {
//                //TODO: this is kinda busted???
//                /*entry.setOffsetY(scrollTweener.get() * ((scrollTweener.get() - scrollTweener.min) / (entry.getY() - scrollTweener.min)));*/
//                entry.setOffsetY(scrollTweener.get() - (scrollTweener.get() * Math.abs(((scrollTweener.get() - scrollTweener.min) / (-entry.getY())))));
//            } else if (scrollTweener.get() > scrollTweener.max) {
//                entry.setOffsetY(scrollTweener.get() + (scrollTweener.get() * Math.abs(((scrollTweener.get() - scrollTweener.max) / entry.getY()))));
//                /*entry.setOffsetY(scrollTweener.get() * (scrollTweener.get() / entry.getY()));*/
//            } else {
//            }
			//TODO: sticky groups
			entry.setOffsetY(scrollTweener.get());
			entry.setOffsetX(-width * categoryTweener.get());
		}
	}

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x) {
		int addY = 0;
		Option<?> option = optionContext.option();
		ClickableWidget widget = option.makeWidget(0, 0, 250, VERTICAL_PADDING / 2);

		PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x, y + addY, option::getDescription);
		this.addDrawableChild(pw);
		evilLayout.put(optionContext, pw);
		addY += VERTICAL_PADDING;

		for (OptionContext<?> child : optionContext.childOptions()) {
			addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
		}

		return addY;
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		updateTweeners();
		super.render(drawContext, mouseX, mouseY, tickDelta);
		drawCommon(drawContext, mouseX, mouseY, tickDelta);

		for (PrecisePositionWidgetWrapper<?> ppww : evilLayout.values()) {
			if (ppww.isMouseOver(mouseX, mouseY)) {
				descriptionText = ppww.getHoverMessage().get();
				animateHoverDescription = true;
				break;
			}
			animateHoverDescription = false;
		}
		if (this.tabs.length > 1) {
			if (tabNavigationWidget.isMouseOver(mouseX, mouseY)) {
				tabHoldTicks = 10;
			}
		}


		if (this.tabs.length > 1) {
			newMatrixScope(drawContext, matrix3x2fStack -> {
				matrix3x2fStack.translate(0, -26 * categoryHoverProgress);
				tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
			});
		}
		if (this.tabs.length > 1) {
			scrollTweener.min = -heights[tabNavigationWidget.getCurrentTabIndex()];
		}
	}

	@Override
	public void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		//TODO: this sucks, and also lags behind by a frame ?
		double mX, mY;
		mX = MinecraftClient.getInstance().mouse.getScaledX(MinecraftClient.getInstance().getWindow());
		mY = MinecraftClient.getInstance().mouse.getScaledY(MinecraftClient.getInstance().getWindow());
		//Cursor glow
		RenderUtils.fillEvil(drawContext, (float) mX, (float) mY, (float) (mX + 2.5f), (float) (mY + 2.5F), MainConfig.primaryCol);
		newMatrixScope(drawContext, stack -> {
			stack.translate(-mouseX * 0.1F, -mouseY * 0.1F);
			stack.translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
			stack.scale(2.5F, 2.5F);
			drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
		});


		drawCommon(drawContext, mouseX, mouseY, tickDelta);

		if (this.tabs.length > 1) {
			newMatrixScope(drawContext, stack -> {
				stack.translate(0, -26 * categoryHoverProgress);

				tabNavigationWidget.renderForBackgroundLayer(drawContext);
			});
		}
		drawContext.createNewRootLayer();
		super.renderBackground(drawContext, mouseX, mouseY, tickDelta);
	}

	private void drawCommon(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		drawLines(drawContext);
		setWidgetOffsets();
		evilLayout.values().forEach(entry -> entry.render(drawContext, mouseX, mouseY, tickDelta));
		newMatrixScope(drawContext, stack -> {
			stack.translate((float) (drawContext.getScaledWindowWidth() / 2), (float) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))));
			drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, 0, 0, MainConfig.primaryCol);
		});
	}

	private void forEachOption(Consumer<OptionHolder> consumer) {
		//run a function for every top-level option, including standalone options
		for (ConfigCategory c : config.categories()) {
			for (OptionHolder o : c.elements()) {
				if (o instanceof ConfigSection) {
					for (OptionHolder opt : o.childOptions()) {
						consumer.accept(opt);
					}
				} else {
					if (!o.childOptions().isEmpty()) {
						consumer.accept(o);
					}
				}
			}
		}
	}

	private void drawLines(DrawContext drawContext) {
		forEachOption(optionHolderV3 -> drawLinesForOption(drawContext, optionHolderV3));
	}

	private void drawLinesForOption(DrawContext drawContext, OptionHolder o) {
		if (o.childOptions().isEmpty()) return;
		PrecisePositionWidgetWrapper<?> mainWidget = evilLayout.get(o);
		PrecisePositionWidgetWrapper<?> lastChildWidget = evilLayout.get(o.childOptions().getLast());
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight(), (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F) + 1, MainConfig.bgColor);
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1, (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F), MainConfig.primaryCol);
		for (OptionHolder opt : o.childOptions()) {
			PrecisePositionWidgetWrapper<?> optionWidget = evilLayout.get(opt);
			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) evilLayout.get(opt).getRealX(), (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + 1, MainConfig.bgColor);
			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) evilLayout.get(opt).getRealX(), (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F, MainConfig.primaryCol);
			drawLinesForOption(drawContext, opt);
		}
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		if (keyInput.hasCtrl() && keyInput.getKeycode() == GLFW.GLFW_KEY_F) {

		}
		return super.keyPressed(keyInput);
	}

	private void updateTweeners() {
		this.scrollTweener.update();
		this.categoryTweener.update();
		//TODO: convert to tweeners
		categoryHoverProgress = (float) ease(categoryHoverProgress, tabHoldTicks > 0 ? 0 : 1, 15);
		int yeah = tabs.length > 1 ? tabNavigationWidget.getCurrentTabIndex() : 0;
		screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);
		descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animateHoverDescription ? 1 : 0, 15);
	}

	@Override
	public void tick() {
		if (this.tabs.length > 1) {
			tabNavigationWidget.tick();
		}
		tabHoldTicks = Math.clamp(tabHoldTicks - 1, 0, 100);
		lastScrollTicks++;
		if (lastScrollTicks > 2) {
			scrollTweener.setInteractionState(false);
		}
	}

	@Override
	public void close() {
		setFocused(null);
		MinecraftClient.getInstance().setScreen(parent);
		GooberLibApi.save(modId, config);
	}

	@Override
	public void resize(int i, int j) {
		int selectedTab = 0;
		if (this.tabs.length > 1) {
			selectedTab = tabNavigationWidget.getCurrentTabIndex();
		}
		super.resize(i, j);
		if (this.tabs.length > 1) {
			tabNavigationWidget.selectTab(selectedTab == -1 ? 0 : selectedTab, false);
		}
		setFocused(null);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		boolean b = super.mouseClicked(click, bl);
		if (!b) {
			setFocused(null);
		}
		return b;
	}

	@Override
	public boolean mouseDragged(Click click, double d, double e) {
		if (super.mouseDragged(click, d, e)) return true;

		if (tabNavigationWidget != null && click.button() == 0 && !tabNavigationWidget.isMouseOver(d, e)) {
			lastScrollTicks = 0;
			scrollTweener.setInteractionState(true);
			if ((scrollProgress < scrollTweener.min && e < 0) || (scrollProgress > scrollTweener.max && e > 0)) {
				scrollProgress += e * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
			} else {
				scrollProgress += e;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		boolean yeah = super.mouseScrolled(d, e, f, g);
		if (!yeah) {
			lastScrollTicks = 0;
			scrollTweener.setInteractionState(true);
			if (tabNavigationWidget != null && !tabNavigationWidget.isMouseOver(d, e)) {
				if ((scrollProgress < scrollTweener.min && g < 0) || (scrollProgress > scrollTweener.max && g > 0)) {
					scrollProgress += g * 15 * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
				} else {
					scrollProgress += g * 15;
				}
			}
		}
		return yeah;
	}
}
