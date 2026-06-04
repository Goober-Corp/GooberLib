package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;

public class Vec3iOption extends BaseOption<Vec3iOption> {
	private final Vec3i defaultValue;
	private Vec3i value;

	public Vec3iOption(Component name, Function<Vec3iOption, Component> description, Vec3i defaultValue, WidgetProvider<Vec3iOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec3iOption(String name, String description, Vec3i defaultValue) {
		this(Component.literal(name), _ -> Component.literal(description), defaultValue, null);
	}

	public Vec3iOption(String name, String description) {
		this(Component.literal(name), _ -> Component.literal(description), Vec3i.ZERO, null);
	}

	public Vec3iOption(String name, String description, Vec3i defaultValue, WidgetProvider<Vec3iOption> provider) {
		this(Component.literal(name), _ -> Component.literal(description), defaultValue, provider);
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
