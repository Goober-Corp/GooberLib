package com.goobercorp.gooberlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GooberConfig {
	/// The mod id
	String modId();

	/// @implNote If additionalClasses is the default (`{}`), then will look for a field of type GooberConfigBuilder. If this does not exist, it will create a config based on the return value of ConfigCategory.ofClass() for the class this was annotated with. <br>If additionalClasses is not empty, it will create a config based on the return value of ConfigCategory.ofClass() for each class in here *and* the class this was annotated with
	Class<?>[] additionalClasses() default {};

	/// Is only used if MAGIC is in use
	String title() default "";

	/// Whether to "late" load this
	boolean lazy() default false;
}
