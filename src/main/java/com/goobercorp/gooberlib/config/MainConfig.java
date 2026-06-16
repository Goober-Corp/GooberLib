package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.misc.LabelOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.FloatOption;
import com.goobercorp.gooberlib.screen.ShowcaseScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

@GooberConfig(modId = "gooberlib")
public class MainConfig {

	public static int primaryCol = 0xFFffaf5e;
	public static int shadowCol = 0xFF3f2b17;
	public static final int bgColor = 0x80000000;
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling");
	public static final BooleanOption HIDE_TABS = new BooleanOption("Hide tabs", true);

	public static final BooleanOption PPWW_BOUNDS = new BooleanOption("Bounds for PPWW");
	public static final ButtonOption REDISCOVER = new ButtonOption("Rediscover configs", GooberLibEntrypoint::init);
	public static final ButtonOption SHOWCASE = new ButtonOption("Open showcase screen", () -> Minecraft.getInstance().setScreen(new ShowcaseScreen()));
	public static final HotkeyOption HOTKEY = new HotkeyOption("Rediscover configs (hotkey)", "", "LEFT_CONTROL, r", 5, GooberLibEntrypoint::init);
	public static final BooleanOption EXPERIMENTAL_DUAL_COLUMN_LAYOUT = new BooleanOption("Dual Column Layout", "don't tell kr1v...");
	public static final BooleanOption DEBUG_GUIDELINES = new BooleanOption("Guidelines");
	public static final BooleanOption CLOSE_SCREEN_ON_EXCEPTION = new BooleanOption("Close gooberlib config screens on exception");
	public static final BooleanOption WOKE = new BooleanOption("Woke mode").setOnValueChange(b -> {
		if (!b.getValue()) primaryCol = 0xFFffaf5e;
	});
	public static final BooleanOption BACKGROUND_GLOW = new BooleanOption("Background Glow");
	public static final BooleanOption CATEGORY_ANIMATIONS = new BooleanOption("Category Animations");

	public static final FloatOption WOKE_STRENGTH = new FloatOption("Wokeness strength", 0.5F, 0F, 1F, WidgetProviders.numberSliderWithFormatter(floatOption -> (int) (floatOption.value * 100) + "%"));

	public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("GooberLib", b -> {
		b.category("Visual", category -> {
			category.options(ENABLE_INFINITE_TAB_SCROLLING, HIDE_TABS, EXPERIMENTAL_DUAL_COLUMN_LAYOUT, BACKGROUND_GLOW, CATEGORY_ANIMATIONS);
			category.optionWithChildren(WOKE, WOKE_STRENGTH);
		});
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			b.category("Developer", category -> {
				category.options(PPWW_BOUNDS, REDISCOVER, SHOWCASE, HOTKEY, DEBUG_GUIDELINES, CLOSE_SCREEN_ON_EXCEPTION);
				category.option(new LabelOption("LARP!!!"));
				category.option(new LabelOption(Component.literal("meow meow").withColor(0xFFFF00FF)));
			});
		}
	});
}