package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;
import com.goobercorp.gooberlib.gui.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.gui.GroupTextWidget;
import com.goobercorp.gooberlib.gui.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;

public class GooberScreen extends Screen {
	private static final int VERTICAL_PADDING = 32;
	private static final int HORIZONTAL_PADDING = 32;
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
	private final Tweener scrollTweener = new Tweener(() -> scrollProgress);
	private final Tweener categoryTweener = new Tweener(() -> screenCategoryAnimationState);
	private final HashMap<OptionHolderV3, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	private final Tab[] tabs;
	AtomicBoolean animate = new AtomicBoolean(false);
	String modid;

	public GooberScreen(BuiltConfig config, Screen parent, String modid) {
		super(config.title());
		this.config = config;
		this.parent = parent;
		this.modid = modid;

		this.tabs = new GridScreenTab[config.categories().size()];
		for (int i = 0; i < config.categories().size(); i++) {
			tabs[i] = new GridScreenTab(config.categories().get(i).metadata().name());
		}

	}

	@Override
	public void tick() {
		tabHoldTicks = Math.clamp(tabHoldTicks - 1, 0, 100);
	}

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x) {
		int addY = 0;
		Option<?> option = optionContext.option();
		ClickableWidget widget = option.getWidgetProvider().makeWidget(option, 0, 0, 125, VERTICAL_PADDING / 2.0);

		PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x, y + addY, option.description());
		this.addDrawableChild(pw);
		evilLayout.put(optionContext, pw);
		addY += VERTICAL_PADDING;

		for (OptionContext<?> child : optionContext.childOptions()) {
			addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
		}

		return addY;
	}

	@Override
	protected void init() {
		this.tabNavigationWidget = this.addSelectableChild(EvilTabNavigationWidget.builder(tabManager, width)
				.tabs(tabs)
				.build());
		this.tabNavigationWidget.init();
		tabNavigationWidget.selectTab(0, false);

		for (ConfigCategory c : config.categories()) {
			int x = (config.categories().indexOf(c) == 0 ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth() * (config.categories().indexOf(c)));
			int y = VERTICAL_PADDING;
			for (OptionHolderV3 o : c.elements()) {
				if (o instanceof ConfigSection(
						MetadataHolder.Metadata metadata,
						List<OptionContext<?>> childOptionContexts
				)) {
					GroupTextWidget t = new GroupTextWidget(
							metadata.name(),
							textRenderer
					);
					PrecisePositionWidgetWrapper<GroupTextWidget> widgetWrapper = new PrecisePositionWidgetWrapper<>(t, x + ((double) MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - (double) textRenderer.getWidth(metadata.name()) / 2, y, metadata.description());
					evilLayout.put(o, widgetWrapper);
					addDrawableChild(widgetWrapper);
					y += VERTICAL_PADDING;
					for (OptionContext<?> yeah : childOptionContexts) {
						y += addOptionWithChildren(yeah, y, x + CHILD_INSET);
					}
				} else {
					y += addOptionWithChildren((OptionContext<?>) o, y, x + CHILD_INSET);
				}
			}
		}
	}

	@Override
	public void close() {
		MinecraftClient.getInstance().setScreen(parent);
		GooberLibApi.save(modid, config);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		return super.mouseClicked(click, bl);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		if (!tabNavigationWidget.isMouseOver(d, e)) {
			scrollProgress = MathHelper.clamp(scrollProgress + g * 15, -1000, 0);
		}
		return super.mouseScrolled(d, e, f, g);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
//        mouseY -= (int) scrollProgress;
//        mouseX -= (int) (-width * screenCategoryAnimationState);
		//TODO: do something about tooltips not lining up
		this.scrollTweener.update();
		this.categoryTweener.update();

		evilLayout.forEach((_, precisePositionWidgetWrapper) -> {
			precisePositionWidgetWrapper.setOffsetY(scrollTweener.get());
			precisePositionWidgetWrapper.setOffsetX(-width * categoryTweener.get());
			precisePositionWidgetWrapper.render(drawContext, mouseX, mouseY, tickDelta);
		});
		drawContext.getMatrices().pushMatrix();
		drawContext.getMatrices().translate((float) (-width * categoryTweener.get()), (float) scrollTweener.get());
		drawLines(drawContext);
		drawContext.getMatrices().popMatrix();
		super.render(drawContext, mouseX, mouseY, tickDelta);

		for (PrecisePositionWidgetWrapper<?> yeah : evilLayout.values()) {
			if (yeah.isMouseOver(mouseX, mouseY)) {
				descriptionText = yeah.getHoverMessage();
				animate.set(true);
				break;
			}
			animate.set(false);
		}

		if (tabNavigationWidget.isMouseOver(mouseX, mouseY)) {
			tabHoldTicks = 10;
		}
		categoryHoverProgress = (float) ease(categoryHoverProgress, tabHoldTicks > 0 ? 1 : 0, 15);
		int yeah = ArrayUtils.indexOf(tabs, tabManager.getCurrentTab());
		screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);

		drawContext.getMatrices().pushMatrix();
		drawContext.getMatrices().translate(0, -26 * (1 - categoryHoverProgress));
		tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
		drawContext.getMatrices().popMatrix();

		descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animate.get() ? 1 : 0, 15);
		drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));
	}

	private void drawLines(DrawContext drawContext) {
		for (ConfigCategory c : config.categories()) {
			for (OptionHolderV3 o : c.elements()) {
				if (!(o instanceof ConfigSection)) {
					if (!o.childOptions().isEmpty()) {
						drawLinesForOption(drawContext, o);
					}
				} else {
					for (OptionHolderV3 opt : o.childOptions()) {
						drawLinesForOption(drawContext, opt);
					}
				}
			}
		}
	}

	private void drawLinesForOption(DrawContext drawContext, OptionHolderV3 o) {
		if (o.childOptions().isEmpty()) return;
		PrecisePositionWidgetWrapper<ClickableWidget> ppww = (PrecisePositionWidgetWrapper<ClickableWidget>) evilLayout.get(o);
		PrecisePositionWidgetWrapper<ClickableWidget> last = (PrecisePositionWidgetWrapper<ClickableWidget>) evilLayout.get(o.childOptions().getLast());
		RenderUtils.drawVerticalLine(drawContext, (float) ppww.getX() + 5, (float) ppww.getY() + ppww.getWrapped().getHeight() - 1, (float) last.getY() + (last.getWrapped().getHeight() / 2F), -1);
		for (OptionHolderV3 opt : o.childOptions()) {
			PrecisePositionWidgetWrapper<ClickableWidget> yeah = (PrecisePositionWidgetWrapper<ClickableWidget>) evilLayout.get(opt);
			RenderUtils.drawHorizontalLine(drawContext, (float) ppww.getX() + 5, (float) evilLayout.get(opt).getX() - 1, (float) yeah.getY() + yeah.getWrapped().getHeight() / 2F, -1);
			drawLinesForOption(drawContext, opt);
		}
	}

	@Override
	public void resize(int i, int j) {
		evilLayout.clear();
		int selectedTab = tabNavigationWidget.getCurrentTabIndex();
		super.resize(i, j);
		tabNavigationWidget.selectTab(selectedTab == -1 ? 0 : selectedTab, false);
	}

	@Override
	public void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		//TODO: make this better
//        mouseY -= (int) scrollProgress;
//        mouseX -= (int) (-width * screenCategoryAnimationState);
		drawContext.getMatrices().pushMatrix();
		drawContext.getMatrices().translate(-mouseX * 0.1F, -mouseY * 0.1F);
		drawContext.getMatrices().translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
		drawContext.getMatrices().scale(2.5F, 2.5F);
		drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
		drawContext.getMatrices().popMatrix();

		drawContext.getMatrices().pushMatrix();
//        drawContext.getMatrices().translate(-width * screenCategoryAnimationState, (float) scrollTweener.get());
//        drawLines(drawContext);
		evilLayout.forEach((optionHolderV3, precisePositionWidgetWrapper) -> {
			precisePositionWidgetWrapper.render(drawContext, mouseX, mouseY, tickDelta);
		});
		drawContext.getMatrices().popMatrix();

		drawContext.getMatrices().pushMatrix();
		drawContext.getMatrices().translate(0, -26 * (1 - categoryHoverProgress));
		tabNavigationWidget.renderForBackgroundLayer(drawContext);
		drawContext.getMatrices().popMatrix();

		drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));

		drawContext.createNewRootLayer();
		super.renderBackground(drawContext, mouseX, mouseY, tickDelta);
	}

}
