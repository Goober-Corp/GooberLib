package com.goobercorp.gooberlib.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public interface HotkeySettings {
	HotkeySettings GUI_BOTH = of(Gui.GUI, When.BOTH, false, false);
	HotkeySettings BOTH_BOTH = of(Gui.BOTH, When.BOTH, false, false);
	HotkeySettings GUI_PRESS = of(Gui.GUI, When.PRESS, false, false);
	HotkeySettings BOTH_PRESS = of(Gui.BOTH, When.PRESS, false, false);
	HotkeySettings GUI_RELEASE = of(Gui.GUI, When.RELEASE, false, false);
	HotkeySettings IN_GAME_BOTH = of(Gui.IN_GAME, When.BOTH, false, false);
	HotkeySettings BOTH_RELEASE = of(Gui.BOTH, When.RELEASE, false, false);
	HotkeySettings IN_GAME_PRESS = of(Gui.IN_GAME, When.PRESS, false, false);
	HotkeySettings IN_GAME_RELEASE = of(Gui.IN_GAME, When.RELEASE, false, false);
	HotkeySettings GUI_BOTH_ALLOW_EXTRA = of(Gui.GUI, When.BOTH, true, false);
	HotkeySettings BOTH_BOTH_ALLOW_EXTRA = of(Gui.BOTH, When.BOTH, true, false);
	HotkeySettings GUI_PRESS_ALLOW_EXTRA = of(Gui.GUI, When.PRESS, true, false);
	HotkeySettings BOTH_PRESS_ALLOW_EXTRA = of(Gui.BOTH, When.PRESS, true, false);
	HotkeySettings GUI_RELEASE_ALLOW_EXTRA = of(Gui.GUI, When.RELEASE, true, false);
	HotkeySettings IN_GAME_BOTH_ALLOW_EXTRA = of(Gui.IN_GAME, When.BOTH, true, false);
	HotkeySettings BOTH_RELEASE_ALLOW_EXTRA = of(Gui.BOTH, When.RELEASE, true, false);
	HotkeySettings IN_GAME_PRESS_ALLOW_EXTRA = of(Gui.IN_GAME, When.PRESS, true, false);
	HotkeySettings IN_GAME_RELEASE_ALLOW_EXTRA = of(Gui.IN_GAME, When.RELEASE, true, false);
	HotkeySettings GUI_BOTH_ORDERED = of(Gui.GUI, When.BOTH, false, true);
	HotkeySettings BOTH_BOTH_ORDERED = of(Gui.BOTH, When.BOTH, false, true);
	HotkeySettings GUI_PRESS_ORDERED = of(Gui.GUI, When.PRESS, false, true);
	HotkeySettings BOTH_PRESS_ORDERED = of(Gui.BOTH, When.PRESS, false, true);
	HotkeySettings GUI_RELEASE_ORDERED = of(Gui.GUI, When.RELEASE, false, true);
	HotkeySettings IN_GAME_BOTH_ORDERED = of(Gui.IN_GAME, When.BOTH, false, true);
	HotkeySettings BOTH_RELEASE_ORDERED = of(Gui.BOTH, When.RELEASE, false, true);
	HotkeySettings IN_GAME_PRESS_ORDERED = of(Gui.IN_GAME, When.PRESS, false, true);
	HotkeySettings IN_GAME_RELEASE_ORDERED = of(Gui.IN_GAME, When.RELEASE, false, true);
	HotkeySettings GUI_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.BOTH, true, true);
	HotkeySettings BOTH_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.BOTH, true, true);
	HotkeySettings GUI_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.PRESS, true, true);
	HotkeySettings BOTH_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.PRESS, true, true);
	HotkeySettings GUI_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.RELEASE, true, true);
	HotkeySettings IN_GAME_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.BOTH, true, true);
	HotkeySettings BOTH_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.RELEASE, true, true);
	HotkeySettings IN_GAME_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.PRESS, true, true);
	HotkeySettings IN_GAME_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.RELEASE, true, true);

	static HotkeySettings of(Gui gui, When when, boolean allowExtra, boolean ordered) {
		return (int keyAction, KeyInput keyInput, List<Integer> currentlyPressedKeys, Hotkey thisHotkey) -> {
			boolean ret = true;

			if (gui != Gui.BOTH) {
				ret &= (MinecraftClient.getInstance().currentScreen != null)
						== (gui == Gui.GUI);
			}

			if (when != When.BOTH) {
				ret &= (keyAction == 1)
						== (when == When.PRESS);
			}

			List<Integer> required = Arrays.stream(thisHotkey.keyCodes).filter(i -> i != 0).collect(ArrayList::new, List::add, List::addAll);

			if (ordered) {
				if (allowExtra) {
					int index = 0;

					for (int key : currentlyPressedKeys) {
						if (index < required.size() && key == required.get(index)) {
							index++;
						}
					}

					ret &= index == required.size();
				} else {
					ret &= currentlyPressedKeys.equals(required);
				}
			} else {
				if (allowExtra) {
					ret &= new HashSet<>(currentlyPressedKeys).containsAll(required);
				} else {
					ret &= currentlyPressedKeys.size() == required.size()
							&& new HashSet<>(currentlyPressedKeys).containsAll(required);
				}

			}

			return ret;
		};
	}
	boolean matches(int keyAction, KeyInput keyInput, List<Integer> currentlyPressedKeys, Hotkey thisHotkey);

	enum When {
		BOTH,
		PRESS,
		RELEASE,
	}

	enum Gui {
		BOTH,
		GUI,
		IN_GAME,
	}
}
