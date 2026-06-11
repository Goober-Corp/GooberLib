package com.goobercorp.gooberlib.option.individual.hotkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class HotkeySettings {
	public final Gui gui;
	public final When when;
	public final boolean allowExtra;
	public final OrderedSetting ordered;
	public static final HotkeySettings GUI_BOTH = of(Gui.GUI, When.BOTH, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_BOTH = of(Gui.BOTH, When.BOTH, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_PRESS = of(Gui.GUI, When.PRESS, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_PRESS = of(Gui.BOTH, When.PRESS, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_RELEASE = of(Gui.GUI, When.RELEASE, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_BOTH = of(Gui.IN_GAME, When.BOTH, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_RELEASE = of(Gui.BOTH, When.RELEASE, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_PRESS = of(Gui.IN_GAME, When.PRESS, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_RELEASE = of(Gui.IN_GAME, When.RELEASE, false, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_BOTH_ALLOW_EXTRA = of(Gui.GUI, When.BOTH, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_BOTH_ALLOW_EXTRA = of(Gui.BOTH, When.BOTH, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_PRESS_ALLOW_EXTRA = of(Gui.GUI, When.PRESS, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_PRESS_ALLOW_EXTRA = of(Gui.BOTH, When.PRESS, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_RELEASE_ALLOW_EXTRA = of(Gui.GUI, When.RELEASE, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_BOTH_ALLOW_EXTRA = of(Gui.IN_GAME, When.BOTH, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings BOTH_RELEASE_ALLOW_EXTRA = of(Gui.BOTH, When.RELEASE, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_PRESS_ALLOW_EXTRA = of(Gui.IN_GAME, When.PRESS, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings IN_GAME_RELEASE_ALLOW_EXTRA = of(Gui.IN_GAME, When.RELEASE, true, OrderedSetting.UNORDERED);
	public static final HotkeySettings GUI_BOTH_ORDERED = of(Gui.GUI, When.BOTH, false, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_BOTH_ORDERED = of(Gui.BOTH, When.BOTH, false, OrderedSetting.ORDERED);
	public static final HotkeySettings GUI_PRESS_ORDERED = of(Gui.GUI, When.PRESS, false, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_PRESS_ORDERED = of(Gui.BOTH, When.PRESS, false, OrderedSetting.ORDERED);
	public static final HotkeySettings GUI_RELEASE_ORDERED = of(Gui.GUI, When.RELEASE, false, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_BOTH_ORDERED = of(Gui.IN_GAME, When.BOTH, false, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_RELEASE_ORDERED = of(Gui.BOTH, When.RELEASE, false, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_PRESS_ORDERED = of(Gui.IN_GAME, When.PRESS, false, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_RELEASE_ORDERED = of(Gui.IN_GAME, When.RELEASE, false, OrderedSetting.ORDERED);
	public static final HotkeySettings GUI_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.BOTH, true, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.BOTH, true, OrderedSetting.ORDERED);
	public static final HotkeySettings GUI_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.PRESS, true, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.PRESS, true, OrderedSetting.ORDERED);
	public static final HotkeySettings GUI_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.GUI, When.RELEASE, true, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_BOTH_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.BOTH, true, OrderedSetting.ORDERED);
	public static final HotkeySettings BOTH_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.BOTH, When.RELEASE, true, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_PRESS_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.PRESS, true, OrderedSetting.ORDERED);
	public static final HotkeySettings IN_GAME_RELEASE_ALLOW_EXTRA_ORDERED = of(Gui.IN_GAME, When.RELEASE, true, OrderedSetting.ORDERED);

	public HotkeySettings(Gui gui, When when, boolean allowExtra, OrderedSetting ordered) {
		this.gui = gui;
		this.when = when;
		this.allowExtra = allowExtra;
		this.ordered = ordered;
	}

	public static HotkeySettings of(Gui gui, When when, boolean allowExtra, OrderedSetting ordered) {
		return new HotkeySettings(gui, when, allowExtra, ordered);
	}

	public boolean matches(int keyAction, List<Integer> currentlyPressedKeys, HotkeyOption thisHotkey) {
		boolean ret = true;

		if (gui != Gui.BOTH) {
			ret &= (Minecraft.getInstance().screen != null)
					== (gui == Gui.GUI);
		}

		if (when != When.BOTH) {
			ret &= (keyAction == 1)
					== (when == When.PRESS);
		}

		List<Integer> required = Arrays.stream(thisHotkey.keyCodes).filter(i -> i != -1).collect(ArrayList::new, List::add, List::addAll);

		if (ordered == OrderedSetting.ORDERED) {
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
		} else if (ordered == OrderedSetting.UNORDERED) {
			if (allowExtra) {
				ret &= new HashSet<>(currentlyPressedKeys).containsAll(required);
			} else {
				ret &= currentlyPressedKeys.size() == required.size()
						&& new HashSet<>(currentlyPressedKeys).containsAll(required);
			}

		} /*else if (ordered == OrderedSetting.MODIFIER_FIRST_UNORDERED) {
			
		}*/

		return ret;
	}

	public enum When {
		BOTH,
		PRESS,
		RELEASE,
	}

	public enum Gui {
		BOTH,
		GUI,
		IN_GAME,
	}

	public enum OrderedSetting {
		ORDERED,
		UNORDERED,
//		/// @implNote for a keybind with the setting "left control + f + c", with this setting pressing lctrl, f, then c will activate it, and pressing lctrl, c, then f will also activate it, and nothing else. For the order, all that matters is that the modifiers are first and of the modifiers that they are in order, and that after that all keys are pressed
//		MODIFIER_FIRST_UNORDERED,
	}
}
