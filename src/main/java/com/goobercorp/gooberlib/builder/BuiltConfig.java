package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.screen.GooberScreen;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public record BuiltConfig(Component title, List<ConfigCategory> categories,
                          TriFunction<BuiltConfig, Screen, String, GooberScreen> screenSupplier) {
	public GooberScreen getScreen(Screen parent, String modId) {
		return screenSupplier.apply(this, parent, modId);
	}
}
