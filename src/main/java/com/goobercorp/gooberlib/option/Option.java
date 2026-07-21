package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public interface Option<T extends Option<T>> {
	<S> S serialize(DynamicOps<S> ops);

	<S> void deserialize(DynamicOps<S> ops, S object);

	void onChange();

	Component name();

	Function<T, Component> description();

	Component getDescription();

	// todo: multiple
	T setOnValueChange(ValueChangeCallback<T> t);

	WidgetProvider<T> getWidgetProvider();

	void resetToDefault();

	AbstractWidget makeWidget(int x, int y, int width, int height);

	boolean isEnabled();

	void setEnabled(boolean var);
}
