package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v2.CategoryBuilder;
import com.goobercorp.gooberlib.builder.v2.OptionHolder;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;

import java.util.List;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<OptionHolderV3> elements) {
	public static CategoryBuilder builder(Class<?> classContainingReferencedFields) {
		return new CategoryBuilder(classContainingReferencedFields);
	}
}
