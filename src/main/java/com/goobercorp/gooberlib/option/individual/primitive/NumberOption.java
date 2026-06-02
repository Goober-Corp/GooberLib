package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.Option;

import java.util.function.Predicate;

public interface NumberOption<T extends Option<T>> extends Option<T> {
	Number getNumberValue();

	void setDoubleValue(double n);

	double getDoubleMin();

	double getDoubleMax();

	void setFromString(String s);

	Predicate<String> getPredicate();

	Predicate<String> getImmediatePredicate();
}
