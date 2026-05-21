package com.goobercorp.gooberlib.option.individual.hotkey;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.Gui;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.OrderedSetting;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.When;
import com.goobercorp.gooberlib.util.HotkeyUtil;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HotkeyOption extends BaseOption<HotkeyOption> {
	public int[] keyCodes;
	private final OnPress onPress;
	public HotkeySettings settings;

	public HotkeyOption(String name, String description, String defaultKeys, int maxKeyCount, Runnable onPress) {
		this(Text.of(name), Text.of(description), null, defaultKeys, maxKeyCount, (_) -> {
			onPress.run();
			return true;
		}, HotkeySettings.BOTH_PRESS);
	}

	public HotkeyOption(Text name, Text description, WidgetProvider provider, String defaultKeys, int maxKeyCount, OnPress onPress, HotkeySettings settings) {
		super(name, description, provider);
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

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		S keyCodeSerialized = ops.createIntList(IntStream.of(this.keyCodes));
		S guiSerialised = ops.createString(this.settings.gui.name());
		S whenSerialized = ops.createString(this.settings.when.name());
		S allowExtraSerialized = ops.createBoolean(this.settings.allowExtra);
		S orderedSerialized = ops.createString(this.settings.ordered.name());

		return ops.createList(Stream.of(keyCodeSerialized, guiSerialised, whenSerialized, allowExtraSerialized, orderedSerialized));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		var listResult = ops.getStream(object);
		if (listResult.isSuccess()) {
			var list = listResult.getOrThrow().toList();
			this.keyCodes = ops.getIntStream(list.getFirst()).getOrThrow().toArray();
			Gui gui;
			When when;
			boolean allowExtra;
			OrderedSetting ordered;

			gui = (Gui.valueOf(ops.getStringValue(list.get(1)).getOrThrow()));
			when = (When.valueOf(ops.getStringValue(list.get(2)).getOrThrow()));
			allowExtra = (ops.getBooleanValue(list.get(3)).getOrThrow());
			ordered = (OrderedSetting.valueOf(ops.getStringValue(list.get(4)).getOrThrow()));
			this.settings = HotkeySettings.of(gui, when, allowExtra, ordered);
		}
	}

	public interface OnPress {
		/// @return whether the keybind did something
		boolean press(HotkeyOption hotkey);
	}
}
