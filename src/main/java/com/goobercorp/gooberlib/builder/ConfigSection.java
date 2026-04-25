package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v2.OptionHolder;

import java.util.List;

public record ConfigSection(MetadataHolder.Metadata metadata, List<ConfigOption> childOptions)
        implements OptionHolder {
}
