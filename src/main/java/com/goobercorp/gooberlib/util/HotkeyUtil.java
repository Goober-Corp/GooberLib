package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.misc.Hotkey;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.function.Erase.rethrow;

public class HotkeyUtil {
	public static final BiMap<Integer, String> MAP;
	public static final List<Hotkey> ALL_HOTKEYS = new ArrayList<>();
	public static final List<Integer> PRESSED = new ArrayList<>();

	public static @InputUtil.Keycode int fromName(String individualKey) {
		Integer keyCode = MAP.inverse().get(individualKey);
		if (keyCode == null) throw new IllegalArgumentException("No keycode for " + individualKey);
		return keyCode;
	}

	public static void handleKeyboard(@KeyInput.KeyAction int keyAction, KeyInput keyInput) {
		if (keyAction == 1) PRESSED.add(keyInput.getKeycode());
		else PRESSED.removeIf(i -> i == keyInput.getKeycode());
		main: for (Hotkey hotkey : ALL_HOTKEYS) {
			if (hotkey.settings.matches(keyAction, keyInput, PRESSED, hotkey)) {
				for (int key : hotkey.keyCodes) {
					if (!isPressed(key)) continue main;
				}
				if (hotkey.onPress()) break;
			}
		}
	}

	public static boolean isPressed(int code) {
		if (code == 0) return true;
		return PRESSED.contains(code);
	}

	static {
		BiMap<Integer, String > map = HashBiMap.create();
		try {
			for (Field f : GLFW.class.getDeclaredFields()) {
				int mods = f.getModifiers();
				if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods) &&
						f.getType() == int.class && (f.getName().startsWith("GLFW_KEY_") || f.getName().startsWith("GLFW_MOUSE_BUTTON_"))) {
					String keyName = f.getName().replaceFirst("GLFW_KEY_", "").replaceFirst("GLFW_", "");
					int keyCode = (int) f.get(null);
					map.put(keyCode, keyName);
				}
			}
		} catch (IllegalAccessException e) {
			throw rethrow(e);
		}
		MAP = map;
	}
}
