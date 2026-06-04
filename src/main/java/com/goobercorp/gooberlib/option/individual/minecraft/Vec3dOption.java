package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class Vec3dOption extends BaseOption<Vec3dOption> {
	private final Vec3 defaultValue;
	private Vec3 value;

	public Vec3dOption(Component name, Function<Vec3dOption, Component> description, Vec3 defaultValue, WidgetProvider<Vec3dOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec3dOption(String name, String description, Vec3 defaultValue) {
		this(Component.literal(name), _ -> Component.nullToEmpty(description), defaultValue, null);
	}

	public Vec3dOption(String name, String description) {
		this(Component.literal(name), _ -> Component.literal(description), Vec3.ZERO, null);
	}

	public Vec3dOption(String name, String description, Vec3 defaultValue, WidgetProvider<Vec3dOption> provider) {
		this(Component.literal(name), _ -> Component.literal(description), defaultValue, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec3.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Vec3.CODEC.parse(ops, object).getOrThrow();
	}

	public Vec3 getValue() {
		return value;
	}

	public void setValue(Vec3 newValue) {
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

	public Vec3 getDefaultValue() {
		return defaultValue;
	}

	public double getX() {
		return this.value.x();
	}

	public double getY() {
		return this.value.y();
	}

	public double getZ() {
		return this.value.z();
	}
}
