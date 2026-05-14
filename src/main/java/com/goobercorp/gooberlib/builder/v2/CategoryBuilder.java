package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryBuilder {
	private final Class<?> parentClass;
	private Text name;
    private Text description;

    private final List<OptionHolderV3> elements = new ArrayList<>();

	@Nullable
    private final GooberConfigBuilder parent;

    CategoryBuilder(GooberConfigBuilder parent) {
        this.parent = parent;
		this.parentClass = parent.getConfigClass();
    }

    public CategoryBuilder(Class<?> clazz) {
        this.parentClass = clazz;
		this.parent = null;
    }

    public OptionContext<CategoryBuilder> option(Option option) {
        OptionContext<CategoryBuilder> optionContext = new OptionContext<>(this, option);
        elements.add(optionContext);
        return optionContext;
    }

    public SectionBuilder section(String name, String description) {
        return new SectionBuilder(this, elements::add)
                .name(name)
                .description(description);
    }

    public SectionBuilder section(Text name) {
        return section().name(name);
    }

    public CategoryBuilder name(Text name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder name(String name) {
        return name(Text.literal(name));
    }

    public CategoryBuilder nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    public CategoryBuilder description(Text description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder description(String description) {
        return description(Text.literal(description));
    }

    public CategoryBuilder descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }

    public GooberConfigBuilder build() {
		if (parent == null) throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
        parent.addCategory(new ConfigCategory(new MetadataHolder.Metadata(name, description), elements));
        return parent;
    }

	public ConfigCategory buildCategory() {
		return new ConfigCategory(new MetadataHolder.Metadata(name, description), elements);
	}

    GooberConfigBuilder getParent() {
		if (parent == null) throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
        return parent;
    }

}
