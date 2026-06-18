package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.interfaces.ScreenSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public record BuiltConfig(Component title, List<ConfigCategory> categories,
                          ScreenSupplier screenSupplier) {
	public Screen getScreen(Screen parent, String modId) {
		return screenSupplier.makeScreen(this, parent, modId);
	}
}
