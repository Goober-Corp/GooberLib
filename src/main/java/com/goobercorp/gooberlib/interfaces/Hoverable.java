package com.goobercorp.gooberlib.interfaces;

import net.minecraft.network.chat.Component;

public interface Hoverable {
	Component getHoverMessage(double mouseX, double mouseY);
}
