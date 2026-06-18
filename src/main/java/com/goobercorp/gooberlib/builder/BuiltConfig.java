package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;

public record BuiltConfig(Component title, List<ConfigCategory> categories,
                          TriFunction<BuiltConfig, Screen, String, Screen> screenSupplier) {
	public Screen getScreen(Screen parent, String modId) {
		return screenSupplier.apply(this, parent, modId);
	}
}
