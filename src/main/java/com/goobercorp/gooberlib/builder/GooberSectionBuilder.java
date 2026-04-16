package com.goobercorp.gooberlib.builder;

public interface GooberSectionBuilder extends MetadataHolder<GooberSectionBuilder>, OptionHolder<GooberSectionBuilder> {

    GooberCategoryBuilder build();

}
