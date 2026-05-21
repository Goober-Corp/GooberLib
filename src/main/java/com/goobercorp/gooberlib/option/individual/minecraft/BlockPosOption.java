package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BlockPosOption extends BaseOption<IdentifierOption> {
	private final BlockPos defaultValue;
	private BlockPos value;

	public BlockPosOption(Text name, Text description, BlockPos defaultValue, WidgetProvider provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public BlockPosOption(String name, String description) {
		this(Text.of(name), Text.of(description), BlockPos.ORIGIN, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return BlockPos.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = BlockPos.CODEC.parse(ops, object).getOrThrow();
	}

	public BlockPos getValue() {
		return value;
	}

	public void setValue(BlockPos newValue) {
		if (!this.value.equals(newValue)) {
			this.value = newValue;
			this.onChange();
		}
	}

	public int getX() {
		return value.getX();
	}

	public int getY() {
		return value.getY();
	}

	public int getZ() {
		return value.getZ();
	}

	public void resetToDefault() {
		if (!this.value.equals(this.defaultValue)) {
			setValue(this.defaultValue);
		}
	}

	public BlockPos getDefaultValue() {
		return defaultValue;
	}
}
