package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface ScreenSupplier {
	Screen makeScreen(BuiltConfig config, @Nullable Screen parent, String modId);
}
