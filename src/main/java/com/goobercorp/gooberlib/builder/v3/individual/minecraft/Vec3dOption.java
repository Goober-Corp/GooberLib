package com.goobercorp.gooberlib.builder.v3.individual.minecraft;

import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Vec3dOption extends BaseOption<Vec3dOption> {
	private final Vec3d defaultValue;
	private Vec3d value;

	public Vec3dOption(Text name, Text description, Vec3d defaultValue, WidgetProvider provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public Vec3dOption(String name, String description, Vec3d defaultValue) {
		this(Text.literal(name), Text.literal(description), defaultValue, null);
	}

	public Vec3dOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), Vec3d.ZERO, null);
	}

	public Vec3dOption(String name, String description, Vec3d defaultValue, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), defaultValue, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec3d.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Vec3d.CODEC.parse(ops, object).getOrThrow();
	}

	public Vec3d getValue() {
		return value;
	}

	public void setValue(Vec3d newValue) {
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

	public Vec3d getDefaultValue() {
		return defaultValue;
	}

	public double getX() {
		return this.value.getX();
	}

	public double getY() {
		return this.value.getY();
	}

	public double getZ() {
		return this.value.getZ();
	}
}
