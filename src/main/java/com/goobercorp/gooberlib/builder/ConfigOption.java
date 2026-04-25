package com.goobercorp.gooberlib.builder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ConfigOption(MetadataHolder.Metadata metadata, Type type, Supplier<?> getter, Consumer<?> setter, List<ConfigOption> children) {
}
