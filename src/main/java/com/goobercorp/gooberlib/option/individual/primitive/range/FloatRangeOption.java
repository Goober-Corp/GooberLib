package com.goobercorp.gooberlib.option.individual.primitive.range;

import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import java.util.function.Function;
import java.util.function.Predicate;

public class FloatRangeOption extends BaseOption<FloatRangeOption> implements NumberRangeOption<FloatRangeOption> {
	private final float defaultValueMin;
	private final float defaultValueMax;
	private final float min;
	private final float max;
	public float minValue;
	public float maxValue;

	public FloatRangeOption(Text name, Function<FloatRangeOption, Text> description, float defaultValueMin, float defaultValueMax, float min, float max, WidgetProvider<FloatRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMinValue(float value) {
		this.minValue = value;
	}

	public void setMaxValue(float value) {
		this.maxValue = value;
	}

	public void resetToDefault() {
		setMinValue(defaultValueMin);
		setMaxValue(defaultValueMax);
	}

	@Override
	public double getDoubleMin() {
		return min;
	}

	@Override
	public double getDoubleMax() {
		return max;
	}

	@Override
	public Number getNumberMinValue() {
		return this.minValue;
	}

	@Override
	public Number getNumberMaxValue() {
		return this.maxValue;
	}

	@Override
	public void setMinFromString(String s) {
		try {
			this.setMinValue(Float.parseFloat(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Float.parseFloat(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return WidgetProviders.Predicates.FLOAT;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (float) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (float) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Vec2f.CODEC.encodeStart(ops, new Vec2f(this.minValue, this.maxValue)).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Vec2f yeah = Vec2f.CODEC.parse(ops, object).getOrThrow();
		this.minValue = yeah.x;
		this.maxValue = yeah.y;
	}
}
