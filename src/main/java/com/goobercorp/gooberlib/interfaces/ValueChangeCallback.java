package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.option.Option;

public interface ValueChangeCallback<T extends Option<T>> {
	void onValueChanged(T optionInstance);
}
