package com.goobercorp.gooberlib.builder.v3;

import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.text.Text;

public abstract class BaseOption<V> implements Option<BaseOption<V>> {
	protected Text name;
	protected Text description;
	private ValueChangeCallback<BaseOption<V>> callback;
	private final WidgetProvider provider;

	protected BaseOption(Text name, Text description, WidgetProvider provider) {
		this.name = name;
		this.description = description;
		this.provider = provider;
	}

	@Override
	public Text name() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <O extends BaseOption<V>> O name(Text name) {
		this.name = name;
		return (O) this;
	}

	@Override
	public Text description() {
		return description;
	}

	@SuppressWarnings("unchecked")
	public <O extends BaseOption<V>> O description(Text description) {
		this.description = description;
		return (O) this;
	}

	@Override
	public void onChange() {
		this.callback.onValueChanged(this);
	}

	@Override
	public void setOnValueChange(ValueChangeCallback<BaseOption<V>> t) {
		this.callback = t;
	}

	@Override
	public WidgetProvider getWidgetProvider() {
		return this.provider;
	}
}
