package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public interface Option<T extends Option<T>> {
	<S> S serialize(DynamicOps<S> ops);

	<S> void deserialize(DynamicOps<S> ops, S object);

	void onChange();

	Text name();

	Text description();

	T setOnValueChange(ValueChangeCallback<T> t);

	WidgetProvider<T> getWidgetProvider();

	ClickableWidget makeWidget(int x, int y, double width, double height);
}
