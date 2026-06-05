package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.option.Option;

public interface AdvanceableOption<T extends Option<T>> extends Option<T> {
	void advance();

	void regress();
}
