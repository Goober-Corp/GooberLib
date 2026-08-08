package com.goobercorp.gooberlib.interfaces;

public interface EnabledStateChangeCallback<T> {
	void onStateChanged(T optionInstance, boolean newState);
}
