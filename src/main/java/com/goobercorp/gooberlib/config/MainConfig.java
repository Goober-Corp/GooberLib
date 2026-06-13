package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.screen.ShowcaseScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

@GooberConfig(modId = "gooberlib")
public class MainConfig {

	public static final int primaryCol = 0xFFffaf5e;
	public static final int shadowCol = 0xFF3f2b17;
	public static final int bgColor = 0x80000000;
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling", "");

	public static final BooleanOption PPWW_BOUNDS = new BooleanOption("Bounds for PPWW", "");
	public static final ButtonOption A = new ButtonOption("Rediscover configs", GooberLibEntrypoint::init);
	public static final ButtonOption SHOWCASE = new ButtonOption("Open showcase screen", () -> Minecraft.getInstance().setScreen(new ShowcaseScreen()));
	public static final HotkeyOption HOTKEY = new HotkeyOption("Rediscover configs (hotkey)", "", "LEFT_CONTROL, r", 5, GooberLibEntrypoint::init);
	public static final BooleanOption EXPERIMENTAL_DUAL_COLUMN_LAYOUT = new BooleanOption("Dual Column Layout", "don't tell kr1v...");
	public static final BooleanOption DEBUG_GUIDELINES = new BooleanOption("Guidelines", "");
	public static final BooleanOption CLOSE_SCREEN_ON_EXCEPTION = new BooleanOption("Close gooberlib config screens on exception", "");

	public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("GooberLib", b -> {
		b.category("Main", category -> {
			category.options(ENABLE_INFINITE_TAB_SCROLLING);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				category.options(PPWW_BOUNDS, A, SHOWCASE, HOTKEY, EXPERIMENTAL_DUAL_COLUMN_LAYOUT, DEBUG_GUIDELINES, CLOSE_SCREEN_ON_EXCEPTION);
			}
		});
	});
}