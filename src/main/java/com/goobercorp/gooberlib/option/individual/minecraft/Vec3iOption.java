package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

import net.minecraft.core.Vec3i;

public class Vec3iOption extends BaseOption<Vec3iOption> {
	private final Vec3i defaultValue;
	private Vec3i value;

	public Vec3iOption(CharSequence name, Function<Vec3iOption, CharSequence> description, Vec3i defaultValue, WidgetProvider<Vec3iOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec3iOption(CharSequence name, CharSequence description, Vec3i defaultValue, WidgetProvider<Vec3iOption> provider) {
		this(name, _ -> description, defaultValue, provider);
	}

	public Vec3iOption(CharSequence name, CharSequence description, Vec3i defaultValue) {
		this(name, _ -> description, defaultValue, null);
	}

	public Vec3iOption(CharSequence name, CharSequence description, WidgetProvider<Vec3iOption> provider) {
		this(name, _ -> description, Vec3i.ZERO, provider);
	}

	public Vec3iOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, Vec3i.ZERO, null);
	}

	public Vec3iOption(CharSequence name, Vec3i defaultValue, WidgetProvider<Vec3iOption> provider) {
		this(name, _ -> "", defaultValue, provider);
	}

	public Vec3iOption(CharSequence name, Vec3i defaultValue) {
		this(name, _ -> "", defaultValue, null);
	}

	public Vec3iOption(CharSequence name, WidgetProvider<Vec3iOption> provider) {
		this(name, _ -> "", Vec3i.ZERO, provider);
	}

	public Vec3iOption(CharSequence name) {
		this(name, _ -> "", Vec3i.ZERO, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec3i.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Vec3i.CODEC.parse(ops, object).getOrThrow();
	}

	public Vec3i getValue() {
		return value;
	}

	public void setValue(Vec3i newValue) {
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

	public Vec3i getDefaultValue() {
		return defaultValue;
	}

	public int getX() {
		return this.value.getX();
	}

	public int getY() {
		return this.value.getY();
	}

	public int getZ() {
		return this.value.getZ();
	}
}
