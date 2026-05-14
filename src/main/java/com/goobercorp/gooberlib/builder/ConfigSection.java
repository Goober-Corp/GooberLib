package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v2.OptionHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;

import java.util.List;

public record ConfigSection(MetadataHolder.Metadata metadata, List<OptionHolderV3> childOptions)
        implements OptionHolderV3 {
}
