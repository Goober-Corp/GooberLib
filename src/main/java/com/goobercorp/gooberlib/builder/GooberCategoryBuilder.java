package com.goobercorp.gooberlib.builder;

public interface GooberCategoryBuilder extends MetadataHolder<GooberCategoryBuilder>, OptionHolder<GooberCategoryBuilder> {

    GooberSectionBuilder section();

    GooberConfigBuilder build();

}
