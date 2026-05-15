package com.goobercorp.gooberlib.builder.v3;

import java.util.List;

public interface OptionHolderV3 {
	List<? extends OptionHolderV3> childOptions();
}
