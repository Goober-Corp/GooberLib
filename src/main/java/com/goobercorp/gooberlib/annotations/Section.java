package com.goobercorp.gooberlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Section {
	/// The sections name
	String value();

	/// The sections description
	String sectionDescription() default "";
}