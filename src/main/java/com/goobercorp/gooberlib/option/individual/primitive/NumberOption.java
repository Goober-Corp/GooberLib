package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.Option;

public interface NumberOption<T extends Option<T>> extends Option<T> {
	Number getDoubleValue();

	void setDoubleValue(double n);

	double getDoubleMin();

	double getDoubleMax();
}
