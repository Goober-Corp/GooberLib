package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;

import java.util.List;

public record ConfigSection(MetadataHolder.Metadata metadata, List<OptionContext<?>> childOptionContexts)
		implements OptionHolderV3 {
	@Override
	public List<OptionHolderV3> childOptions() {
		return childOptionContexts.stream().map(OptionHolderV3.class::cast).toList();
	}
}
