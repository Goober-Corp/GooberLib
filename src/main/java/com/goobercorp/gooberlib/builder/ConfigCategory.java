package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.v2.CategoryBuilder;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;

import java.util.List;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<OptionHolderV3> elements) {
	public static CategoryBuilder builder() {
		return new CategoryBuilder(null);
	}

	public static CategoryBuilder builder(String name, String description) {
		var categoryBuilder = new CategoryBuilder(null);
		categoryBuilder.name(name);
		categoryBuilder.description(description);
		return categoryBuilder;
	}
}
