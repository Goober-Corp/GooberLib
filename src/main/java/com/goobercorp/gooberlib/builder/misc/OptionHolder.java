package com.goobercorp.gooberlib.builder.misc;

import java.util.List;

public interface OptionHolder {
	List<? extends OptionHolder> childOptions();
}
