package com.goobercorp.gooberlib.builder;

import java.util.List;

public record ConfigSection(MetadataHolder.Metadata metadata, List<ConfigOption> options) {
}
