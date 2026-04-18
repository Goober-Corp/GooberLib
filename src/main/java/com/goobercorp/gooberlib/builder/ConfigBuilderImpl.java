package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.ConfigCategory;
import com.google.gson.GsonBuilder;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;

/*

!!! WARNING !!!

GENERIC HELL
BULLSHIT CODE

 */
class ConfigBuilderImpl implements GooberConfigBuilder {
    //TODO where you have a field of a certain type and many options that set fields in that struct
    private final List<ConfigCategory> categories = new ArrayList<>();

    private final Class<?> configClass; // is this even needed?

    private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    private Text title;

    ConfigBuilderImpl() {
        configClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(2).findFirst())
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElseThrow();
    }

    @Override
    public GooberConfigBuilder title(Text title) {
        this.title = title;
        return this;
    }

    @Override
    public GooberConfigBuilder typeAdapter(Type type, Object typeAdapter) {
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        return this;
    }

    @Override
    public GooberCategoryBuilder category() {
        return new CategoryBuilder();
    }

    @Override
    public BuiltConfig build() {
        return new BuiltConfig(gsonBuilder.create(), title, categories);
    }

    class CategoryBuilder implements GooberCategoryBuilder, FieldRegistry {
        private final List<Object> elements = new ArrayList<>();
        private Text name;
        private @Nullable Text description;

        @Override
        public GooberSectionBuilder section() {
            return new SectionBuilder();
        }

        @Override
        public GooberConfigBuilder build() {
            Validate.notNull(name, "name must not be null");

            categories.add(new ConfigCategory(new Metadata(name, description), elements));
            return ConfigBuilderImpl.this;
        }

        @Override
        public GooberCategoryBuilder name(Text name) {
            this.name = name;
            return this;
        }

        @Override
        public GooberCategoryBuilder description(Text description) {
            this.description = description;
            return this;
        }

        @Override
        public GooberFieldBuilder<GooberCategoryBuilder> field(String name) {
            return new OptionBuilder<>(this, name);
        }

        @Override
        public void register(String name, Metadata metadata, List<ConfigOption> children) {
            elements.add(new ConfigOption(metadata, name, children));
        }

        class SectionBuilder implements GooberSectionBuilder, FieldRegistry {
            private final List<ConfigOption> options = new ArrayList<>();

            private Text name;
            private @Nullable Text description;


            @Override
            public GooberCategoryBuilder build() {
                Validate.notNull(name, "name must not be null");

                elements.add(new ConfigSection(new Metadata(name, description), options));
                return CategoryBuilder.this;
            }

            @Override
            public GooberSectionBuilder name(Text name) {
                this.name = name;
                return this;
            }

            @Override
            public GooberSectionBuilder description(Text description) {
                this.description = description;
                return this;
            }

            @Override
            public GooberFieldBuilder<GooberSectionBuilder> field(String name) {
                return new OptionBuilder<>(this, name);
            }

            @Override
            public void register(String name, Metadata metadata, List<ConfigOption> children) {
                options.add(new ConfigOption(metadata, name, children));
            }
        }
    }

    static class OptionBuilder<TB extends OptionHolder<TB>, T extends OptionHolder<TB> & FieldRegistry> implements GooberFieldBuilder<TB> {
        private final T parent;

        private final List<ConfigOption> children = new ArrayList<>();

        private final String fieldName;
        private Text name;
        private @Nullable Text description;

        OptionBuilder(T parent, String fieldName) {
            this.parent = parent;
            this.fieldName = fieldName;
        }

        @Override
        public GooberChildAppender<TB> withChildren() {
            return new ChildAppender();
        }

        @Override
        @SuppressWarnings("unchecked")
        public TB build() {
            Validate.notNull(name, "name must not be null");

            parent.register(fieldName, new Metadata(name, description), children);
            return (TB) parent;
        }

        @Override
        public GooberFieldBuilder<TB> name(Text name) {
            this.name = name;
            return this;
        }

        @Override
        public GooberFieldBuilder<TB> description(Text description) {
            this.description = description;
            return this;
        }

        class ChildAppender implements GooberChildAppender<TB>, FieldRegistry {
            @Override
            public GooberFieldBuilder<GooberChildAppender<TB>> field(String name) {
                return new OptionBuilder<>(this, name);
            }

            @Override
            public GooberFieldBuilder<GooberChildAppender<TB>> child(String fieldName) {
                return field(fieldName);
            }

            @Override
            public void register(String name, Metadata metadata, List<ConfigOption> children) {
                children.add(new ConfigOption(metadata, name, children));
            }

            @Override
            @SuppressWarnings("unchecked")
            public TB build() {
                Validate.notNull(name, "name must not be null");

                parent.register(fieldName, new Metadata(name, description), children);
                return (TB) parent;
            }
        }
    }

    interface FieldRegistry {
        void register(String name, MetadataHolder.Metadata metadata, List<ConfigOption> children);
    }
}
