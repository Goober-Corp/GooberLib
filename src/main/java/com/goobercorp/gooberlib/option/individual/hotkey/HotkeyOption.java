package com.goobercorp.gooberlib.option.individual.hotkey;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.Gui;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.OrderedSetting;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeySettings.When;
import com.goobercorp.gooberlib.util.HotkeyUtil;
import com.mojang.serialization.DynamicOps;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HotkeyOption extends BaseOption<HotkeyOption> {
	public int[] keyCodes;
	private final OnPress onPress;
	public HotkeySettings settings;
	public boolean editing = false;

	public HotkeyOption(CharSequence name, CharSequence description, String defaultKeys, int maxKeyCount, Runnable onPress) {
		this(name, _ -> description, null, defaultKeys, maxKeyCount, (_) -> {
			onPress.run();
			return true;
		}, HotkeySettings.BOTH_PRESS);
	}

	public HotkeyOption(CharSequence name, Function<HotkeyOption, CharSequence> description, WidgetProvider<HotkeyOption> provider, String defaultKeys, int maxKeyCount, OnPress onPress, HotkeySettings settings) {
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
		if (!editing) {
			return this.onPress.press(this);
		}
		return false;
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
			var codes = ops.getIntStream(list.getFirst()).getOrThrow().toArray();
			if (codes.length > keyCodes.length) keyCodes = codes;
			else {
				Arrays.fill(this.keyCodes, -1);
				System.arraycopy(codes, 0, this.keyCodes, 0, codes.length);
			}
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

	public String keyCodesString() {
		StringBuilder b = new StringBuilder();
		for (int keycode : keyCodes) {
			if (keycode == -1) break;
			b.append(HotkeyUtil.MAP.get(keycode));
			b.append(" + ");
		}
		if (b.isEmpty()) return "None";
		b.delete(b.length() - 3, b.length());
		return b.toString();
	}

	public void addCode(int code) {
		long lastIndex = IntStream.of(keyCodes).filter(i -> i != -1).count();
		if (lastIndex == keyCodes.length) return;
		if (ArrayUtils.contains(keyCodes, code)) return;
		keyCodes[(int) lastIndex] = code;
	}

	public void clearKeyCodes() {
		Arrays.fill(this.keyCodes, -1);
	}

	public interface OnPress {
		/// @return whether the keybind did something
		boolean press(HotkeyOption hotkey);
	}
}
