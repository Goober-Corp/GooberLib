package com.goobercorp.gooberlib.misc;

import com.goobercorp.gooberlib.util.HotkeyUtil;

import java.util.Locale;

public class Hotkey {
	public final int[] keyCodes;
	private final OnPress onPress;
	public HotkeySettings settings;

	public Hotkey(String defaultKeys, int maxKeyCount, Runnable onPress) {
		this(defaultKeys, maxKeyCount, (_) -> {onPress.run(); return true;}, HotkeySettings.BOTH_PRESS);
	}

	public Hotkey(String defaultKeys, int maxKeyCount, OnPress onPress, HotkeySettings settings) {
		String[] individualKeys = defaultKeys.replaceAll("\\s+", "").split(",");
		if (individualKeys.length > maxKeyCount) {
			throw new IllegalArgumentException("Default keys size (" + individualKeys.length + ") is more than provided max keys (" + maxKeyCount + ")");
		}
		this.onPress = onPress;
		this.settings = settings;
		this.keyCodes = new int[maxKeyCount];
		for (int i = 0; i < individualKeys.length; i++) {
			keyCodes[i] = HotkeyUtil.fromName(individualKeys[i].toUpperCase(Locale.ROOT));
		}
		HotkeyUtil.ALL_HOTKEYS.add(this);
	}

	public boolean onPress() {
		return this.onPress.press(this);
	}

	public interface OnPress {
		/// @return whether the keybind did something
		boolean press(Hotkey hotkey);
	}
}
