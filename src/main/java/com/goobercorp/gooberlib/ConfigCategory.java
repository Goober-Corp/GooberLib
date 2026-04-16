package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.builder.MetadataHolder;

import java.util.List;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<Object> elements) {
}
