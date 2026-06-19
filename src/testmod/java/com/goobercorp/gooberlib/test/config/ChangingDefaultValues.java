package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

// @formatter:off
@GooberConfig(modId = "changing-default-value-test", title = "Changing default values test")
public class ChangingDefaultValues {
	public static final BooleanOption DEFAULT_FALSE = new BooleanOption("Defaults to false");
	static { GooberLibApi.Defaults.booleanDefault = true; }
	public static final BooleanOption DEFAULT_TRUE = new BooleanOption("Defaults to true");
	static { GooberLibApi.Defaults.booleanDefault = false; }
	public static final BooleanOption DEFAULT_FALSE_2 = new BooleanOption("Defaults to false again");
}
