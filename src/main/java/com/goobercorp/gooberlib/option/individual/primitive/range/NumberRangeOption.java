package com.goobercorp.gooberlib.option.individual.primitive.range;

import com.goobercorp.gooberlib.option.Option;

import java.util.function.Predicate;

public interface NumberRangeOption<T extends Option<T>> extends Option<T> {
	double getDoubleMin();

	double getDoubleMax();

	Number getNumberMinValue();

	Number getNumberMaxValue();

	// todo: make this work (eg 4-5)
	void setMinFromString(String s);

	void setMaxFromString(String s);

	Predicate<String> getPredicate();

	void setMinDoubleValue(double v);

	void setMaxDoubleValue(double v);
}
