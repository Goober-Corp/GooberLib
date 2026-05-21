package com.goobercorp.gooberlib.builder.v3;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public abstract class BaseOption<V extends BaseOption<V>> implements Option<V> {
	protected Text name;
	protected Text description;
	private ValueChangeCallback<V> callback;
	private final WidgetProvider provider;

	protected BaseOption(Text name, Text description, @Nullable WidgetProvider provider) {
		this.name = name;
		this.description = description;
		// todo: this needs testing for EnumOption due to it being a generic class
		@SuppressWarnings("unchecked")
		var optionClass = (Class<? extends Option<?>>) this.getClass();
		this.provider = provider == null
				? GooberLibApi.getDefaultWidgetProvider(optionClass)
				: provider;
	}

	@Override
	public Text name() {
		return name;
	}

	@Override
	public Text description() {
		return description;
	}

	@Override
	public void onChange() {
		if (callback != null) {
			//noinspection unchecked
			this.callback.onValueChanged((V) this);
		}
	}

	@Override
	public V setOnValueChange(ValueChangeCallback<V> t) {
		this.callback = t;
		//noinspection unchecked
		return (V) this;
	}

	@Override
	public WidgetProvider getWidgetProvider() {
		if (this.provider == null)
			throw new IllegalStateException("??");
		return this.provider;
	}
}
