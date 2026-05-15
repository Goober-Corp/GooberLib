package com.goobercorp.gooberlib.builder.v3;

import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public interface Option<T extends Option<T>> {
	<S> S serialize(DynamicOps<S> ops);

	// TODO: better error handling for deserialization errors
	<S> void deserialize(DynamicOps<S> ops, S object);

	default void onChange() {
	}

	Text name();

	Text description();

	void setOnValueChange(ValueChangeCallback<T> t);

	WidgetProvider getWidgetProvider();
}
