package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.builder.v3.Option;

public interface ValueChangeCallback<T extends Option<T>> {
	void onValueChanged(T optionInstance);
}
