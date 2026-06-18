package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ModListScreen extends GooberScreen {
	public ModListScreen(BuiltConfig config, Screen parent, String modId) {
		super(config, parent, modId);
	}

	@Override
	public void init() {
		showTabs = false;
		categoryWidgets.clear();

		var cat = new CategoryWidget(ConfigCategory.builder("Mod List", b -> GooberLibApi.getAllConfigs().forEach((s, config1) -> {
			if (s.equals(modId)) {
				return;
			}
			//TODO: decide whether to show title or modid. or both. idk
			b.option(new ButtonOption(config1.title(), () -> {
				Screen screen = GooberLibApi.getScreenFor(s, this);
				if (screen instanceof GooberScreen gs) {
					gs.mouseXTweener = this.mouseXTweener;
					gs.mouseYTweener = this.mouseYTweener;
				}
				Minecraft.getInstance().setScreen(screen);
			}));
		})), 0, 0, width, height);
		PrecisePositionWidgetWrapper<CategoryWidget> pw = new PrecisePositionWidgetWrapper<>(cat, 0, VERTICAL_PADDING, () -> Util.fromChars(""));
		this.addWidget(pw);
		categoryWidgets.add(pw);
		setWidgetOffsets();
	}
}
