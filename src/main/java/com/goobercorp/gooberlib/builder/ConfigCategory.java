package com.goobercorp.gooberlib.builder;

import java.util.List;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<Object> elements) {
}
