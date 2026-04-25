package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v2.OptionHolder;

import java.util.List;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<OptionHolder> elements) {
}
