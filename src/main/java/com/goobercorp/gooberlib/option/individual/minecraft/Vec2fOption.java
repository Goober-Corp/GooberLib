package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import java.util.function.Function;

public class Vec2fOption extends BaseOption<Vec2fOption> {
	private final Vec2f defaultValue;
	private Vec2f value;

	public Vec2fOption(Text name, Function<Vec2fOption, Text> description, Vec2f defaultValue, WidgetProvider<Vec2fOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec2fOption(String name, String description, Vec2f defaultValue) {
		this(Text.literal(name), _ -> Text.literal(description), defaultValue, null);
	}

	public Vec2fOption(String name, String description) {
		this(Text.literal(name), _ -> Text.literal(description), Vec2f.ZERO, null);
	}

	public Vec2fOption(String name, String description, Vec2f defaultValue, WidgetProvider<Vec2fOption> provider) {
		this(Text.literal(name), _ -> Text.literal(description), defaultValue, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec2f.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Vec2f.CODEC.parse(ops, object).getOrThrow();
	}

	public Vec2f getValue() {
		return value;
	}

	public void setValue(Vec2f newValue) {
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

	public Vec2f getDefaultValue() {
		return defaultValue;
	}

	public float getX() {
		return this.value.x;
	}

	public float getY() {
		return this.value.y;
	}
}
