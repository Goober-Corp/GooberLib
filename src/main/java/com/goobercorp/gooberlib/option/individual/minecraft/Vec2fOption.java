package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

import net.minecraft.world.phys.Vec2;

public class Vec2fOption extends BaseOption<Vec2fOption> {
	private final Vec2 defaultValue;
	private Vec2 value;

	public Vec2fOption(CharSequence name, Function<Vec2fOption, CharSequence> description, Vec2 defaultValue, WidgetProvider<Vec2fOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec2fOption(CharSequence name, CharSequence description, Vec2 defaultValue) {
		this(name, _ -> description, defaultValue, null);
	}

	public Vec2fOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, Vec2.ZERO, null);
	}

	public Vec2fOption(CharSequence name, CharSequence description, Vec2 defaultValue, WidgetProvider<Vec2fOption> provider) {
		this(name, _ -> description, defaultValue, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec2.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Vec2.CODEC.parse(ops, object).getOrThrow();
	}

	public Vec2 getValue() {
		return value;
	}

	public void setValue(Vec2 newValue) {
		if (!this.value.equals(newValue)) {
			this.value = newValue;
			this.onChange();
		}
	}

	public void resetToDefault() {
		if (!this.value.equals(this.defaultValue)) {
			setValue(this.defaultValue);
		}
	}

	public Vec2 getDefaultValue() {
		return defaultValue;
	}

	public float getX() {
		return this.value.x;
	}

	public float getY() {
		return this.value.y;
	}
}
