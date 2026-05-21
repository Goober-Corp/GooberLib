package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.screen.GooberScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;

public record BuiltConfig(Text title, List<ConfigCategory> categories,
                          TriFunction<BuiltConfig, Screen, String, GooberScreen> screenSupplier) {
	public GooberScreen getScreen(Screen parent, String modId) {
		return screenSupplier.apply(this, parent, modId);
	}
}
