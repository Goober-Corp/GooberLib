package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;

import java.util.List;

public record ConfigSection(MetadataHolder.Metadata metadata, List<OptionContext<?>> childOptions)
		implements OptionHolderV3 {
}
