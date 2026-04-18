package com.goobercorp.gooberlib.builder;

import java.util.List;

public record ConfigOption(MetadataHolder.Metadata metadata, String field, List<ConfigOption> children) {
}
