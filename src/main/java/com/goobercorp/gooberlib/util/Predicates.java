package com.goobercorp.gooberlib.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Predicates {
	private static Predicate<String> falseIfException(Consumer<String> sEr, Class<? extends Throwable> clazz) {
		return s -> {
			try {
				sEr.accept(s);
				return true;
			} catch (Throwable t) {
				if (clazz.isInstance(t)) {
					return false;
				} else {
					throw t;
				}
			}
		};
	}

	public static final Predicate<String> COLOR = falseIfException(Long::decode, NumberFormatException.class);
	public static final Predicate<String> IDENTIFIER = falseIfException(Identifier::of, InvalidIdentifierException.class);
	public static final Predicate<String> INTEGER = falseIfException(Integer::parseInt, NumberFormatException.class);
	public static final Predicate<String> DOUBLE = falseIfException(Double::parseDouble, NumberFormatException.class);
	public static final Predicate<String> FLOAT = falseIfException(Float::parseFloat, NumberFormatException.class);
	public static final Predicate<String> LONG = falseIfException(Long::parseLong, NumberFormatException.class);
	public static final Predicate<String> SHORT = falseIfException(Short::parseShort, NumberFormatException.class);
	public static final Predicate<String> BYTE = falseIfException(Byte::parseByte, NumberFormatException.class);
}
