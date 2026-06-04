package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public class Vec2fOption extends BaseOption<Vec2fOption> {
	private final Vec2 defaultValue;
	private Vec2 value;

	public Vec2fOption(Component name, Function<Vec2fOption, Component> description, Vec2 defaultValue, WidgetProvider<Vec2fOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec2fOption(String name, String description, Vec2 defaultValue) {
		this(Component.literal(name), _ -> Component.literal(description), defaultValue, null);
	}

	public Vec2fOption(String name, String description) {
		this(Component.literal(name), _ -> Component.literal(description), Vec2.ZERO, null);
	}

	public Vec2fOption(String name, String description, Vec2 defaultValue, WidgetProvider<Vec2fOption> provider) {
		this(Component.literal(name), _ -> Component.literal(description), defaultValue, provider);
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
