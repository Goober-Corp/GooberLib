package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

import net.minecraft.core.BlockPos;

public class BlockPosOption extends BaseOption<BlockPosOption> {
	private final BlockPos defaultValue;
	private BlockPos value;

	public BlockPosOption(CharSequence name, Function<BlockPosOption, CharSequence> description, BlockPos defaultValue, WidgetProvider<BlockPosOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public BlockPosOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, BlockPos.ZERO, null);
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
