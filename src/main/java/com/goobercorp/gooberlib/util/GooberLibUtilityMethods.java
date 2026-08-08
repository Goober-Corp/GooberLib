package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.option.Option;

import java.util.List;

public class GooberLibUtilityMethods {
	/**
	 * Utility method to make toggling enabled state of multiple options easier
	 *
	 * @param state state to set enabled to
	 * @param list  list of options to set the enabled state of
	 */
	public static void setListEnabled(boolean state, List<Option<?>> list) {
		for (var option : list) {
			option.setEnabled(state);
		}
	}
}
