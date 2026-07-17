package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.gui.option.EvilButtonWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.misc.LabelOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.FloatOption;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.screen.ModListScreen;
import com.goobercorp.gooberlib.screen.ShowcaseScreen;
import com.goobercorp.gooberlib.util.TargetedTweener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.goobercorp.gooberlib.screen.GooberScreen.getRainbow;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

@GooberConfig(modId = "gooberlib")
public class MainConfig {
	public static int primaryCol = 0xFFffaf5e;
	public static int shadowCol = 0xFF3f2b17;
	public static final int bgColor = 0x80000000;

	//	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling");
	public static final BooleanOption HIDE_TABS = new BooleanOption("Hide tabs", true);

	public static final BooleanOption PPWW_BOUNDS = new BooleanOption("Bounds for PPWW");
	public static final ButtonOption REDISCOVER = new ButtonOption("Rediscover configs", GooberLibEntrypoint::init);
	public static final ButtonOption SHOWCASE = new ButtonOption("Open showcase screen", () -> Minecraft.getInstance().setScreen(new ShowcaseScreen()));
	public static final HotkeyOption HOTKEY = new HotkeyOption("Rediscover configs (hotkey)", "", "LEFT_CONTROL, r", 5, GooberLibEntrypoint::init);
	public static final BooleanOption EXPERIMENTAL_DUAL_COLUMN_LAYOUT = new BooleanOption("Dual Column Layout", "don't tell kr1v...");
	public static final BooleanOption DEBUG_GUIDELINES = new BooleanOption("Guidelines");
	public static final BooleanOption CLOSE_SCREEN_ON_EXCEPTION = new BooleanOption("Close gooberlib config screens on exception");
	public static final BooleanOption BACKGROUND_GLOW = new BooleanOption("Background Glow", true);
	public static final BooleanOption CATEGORY_ANIMATIONS = new BooleanOption("Category Animations", true);

	public static final FloatOption WOKE_STRENGTH = new FloatOption("Wokeness strength", 0.5F, 0F, 1F, WidgetProviders.numberSliderWithFormatter(floatOption -> (int) (floatOption.value * 100) + "%"));
	public static final BooleanOption WOKE = new BooleanOption("Woke mode").setOnValueChange(b -> {
		if (!b.getValue()) {
			primaryCol = 0xFFffaf5e;
			shadowCol = ARGB.scaleRGB(primaryCol, 0.25F);
		}
		WOKE_STRENGTH.setEnabled(b.getValue());
	});

	public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("GooberLib", b -> {
		b.category("Visual", category -> {
			category.options(/*ENABLE_INFINITE_TAB_SCROLLING,*/HIDE_TABS, EXPERIMENTAL_DUAL_COLUMN_LAYOUT, BACKGROUND_GLOW, CATEGORY_ANIMATIONS);
			category.optionWithChildren(WOKE, WOKE_STRENGTH);
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			b.category("Developer", category -> {
				category.options(PPWW_BOUNDS, REDISCOVER, SHOWCASE, HOTKEY, DEBUG_GUIDELINES, CLOSE_SCREEN_ON_EXCEPTION);
				category.option(new LabelOption("LARP!!!"));
				category.option(new LabelOption(Component.literal("meow meow").withColor(0xFFFF00FF)));
			});
		}
		b.screenSupplier((config, parent, modid) -> new Screen(Component.literal("GooberLib")) {
			final int textWidth = font.width("GooberLib");
			private final List<PrecisePositionWidgetWrapper<?>> widgets = new ArrayList<>();
			private final TargetedTweener mouseXTweener = new TargetedTweener(20);
			private final TargetedTweener mouseYTweener = new TargetedTweener(20);

			@Override
			protected void init() {
				super.init();
				widgets.clear();

				PrecisePositionWidgetWrapper<AbstractWidget> settings = new PrecisePositionWidgetWrapper<>(new EvilButtonWidget("GooberLib Settings", () -> {
					GooberScreen screen = new GooberScreen(config, this, modid);
					screen.mouseXTweener = this.mouseXTweener;
					screen.mouseYTweener = this.mouseYTweener;
					Minecraft.getInstance().setScreen(screen);
				}, 0, 0, textWidth * 4 + 1, GooberScreen.VERTICAL_PADDING / 2, true), this.width / 2.0 - textWidth * 2 - 1, this.height / 2.0, Component::empty);
				PrecisePositionWidgetWrapper<AbstractWidget> detectedMods = new PrecisePositionWidgetWrapper<>(new EvilButtonWidget("Mod List", () -> {
					ModListScreen screen = new ModListScreen(config, this, modid);
					screen.mouseXTweener = this.mouseXTweener;
					screen.mouseYTweener = this.mouseYTweener;
					Minecraft.getInstance().setScreen(screen);
				}, 0, 0, textWidth * 4 + 1, GooberScreen.VERTICAL_PADDING / 2, true), this.width / 2.0 - textWidth * 2 - 1, this.height / 2.0 + GooberScreen.VERTICAL_PADDING, Component::empty);
				PrecisePositionWidgetWrapper<AbstractWidget> userGuide = new PrecisePositionWidgetWrapper<>(new EvilButtonWidget("User Guide", () -> Util.getPlatform().openUri(URI.create("https://docs.goobercorp.com/userguide")), 0, 0, textWidth * 4 + 1, GooberScreen.VERTICAL_PADDING / 2, true), this.width / 2.0 - textWidth * 2 - 1, this.height / 2.0 + GooberScreen.VERTICAL_PADDING * 2, Component::empty);

				widgets.add(this.addRenderableWidget(settings));
				widgets.add(this.addRenderableWidget(detectedMods));
				widgets.add(this.addRenderableWidget(userGuide));
			}

			@Override
			public void render(GuiGraphics guiGraphics, int i, int j, float f) {
				drawCommon(guiGraphics);
				super.render(guiGraphics, i, j, f);
			}

			@Override
			public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
				double mX, mY;
				mX = Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
				mY = Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());
				if (!(Double.isNaN(mX) || Double.isInfinite(mX) || Double.isNaN(mY) || Double.isInfinite(mY))) {
					mouseXTweener.setTarget(mX);
					mouseYTweener.setTarget(mY);
				}
				mouseXTweener.update();
				mouseYTweener.update();
				if (MainConfig.WOKE.getValue()) {
					MainConfig.primaryCol = (0xFF << 24) | getRainbow(0, 1, MainConfig.WOKE_STRENGTH.value, 1);
					MainConfig.shadowCol = (0xFF << 24) | getRainbow(0, 1, MainConfig.WOKE_STRENGTH.value, 0.25F);
				}
				newMatrixScope(guiGraphics, stack -> {
					stack.translate((float) (-mouseXTweener.get() * 0.1F), (float) (-mouseYTweener.get() * 0.1F));
					stack.translate(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F - 100, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2F - 100);
					stack.scale(2.5F, 2.5F);
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
				});
				widgets.forEach(widget -> widget.render(guiGraphics, i, j, f));
				drawCommon(guiGraphics);
				guiGraphics.nextStratum();
				super.renderBackground(guiGraphics, i, j, f);
			}

			private void drawCommon(GuiGraphics guiGraphics) {
				newMatrixScope(guiGraphics, stack -> {
					stack.translate(width / 2F, height / 4F);
					//tektonikal will write this shit and then still have the balls to tell u "ya but it's centered doe"
					stack.translate(-textWidth * 2, -(font.lineHeight + 1) * 1.625F + 0.25F);
					stack.scale(4);
					guiGraphics.drawString(font, "GooberLib", 0, 0, MainConfig.primaryCol);
				});
				newMatrixScope(guiGraphics, stack -> {
					//noinspection OptionalGetWithoutIsPresent
					String s = "v" + FabricLoader.getInstance().getModContainer(modid).get().getMetadata().getVersion().getFriendlyString();
					stack.translate(width / 2F + textWidth * 2 - font.width(s) / 2F, height / 4F + font.lineHeight + 4);
					guiGraphics.drawString(font, s, 0, 0, MainConfig.primaryCol, true);
//					guiGraphics.drawString(font, "™", 14, -30, MainConfig.primaryCol, true);
				});
			}

			@Override
			public void onClose() {
				this.minecraft.setScreen(parent);
			}
		});
	});
}