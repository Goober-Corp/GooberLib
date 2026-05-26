package com.goobercorp.gooberlib.test.config.others;

import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.EnumOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.primitive.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// todo: overloads
public class Yacl {
	/*	public static final BooleanOption booleanToggle = new BooleanOption(Text.of("Boolean Toggle"), _ -> Text.empty()
				.append(Text.literal("a").styled(style -> style.withHoverEvent(new HoverEvent.ShowText(Text.literal("a")))))
				.append(Text.literal("b").styled(style -> style.withHoverEvent(new HoverEvent.ShowText(Text.literal("b")))))
				.append(Text.literal("c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c").styled(style -> style.withHoverEvent(new HoverEvent.ShowText(Text.literal("c")))))
				.append(Text.literal("e").styled(style -> style.withHoverEvent(new HoverEvent.ShowText(Text.literal("e")))))
				.append(Text.literal("click me").styled(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://isxander.dev"))))), false, null
		); // todo: widget*/
//	public static final BooleanOption customBooleanToggle = new BooleanOption("Custom Boolean Toggle", "You can customize controllers like so! YACL is truly infinitely customizable! This tooltip is long in order to demonstrate the cool, smooth scrolling of these descriptions. Did you know, they are also super clickable?! I know, cool right, YACL 3.x really is amazing."); // todo: widget
	public static final BooleanOption tickbox = new BooleanOption("Tick Box", "There are even alternate methods of displaying the same data type!");

	public static final IntOption intSlider = new IntOption(Text.of("Int Slider"), _ -> Text.empty(), 1, 0, 3, null);
	public static final DoubleOption doubleSlider = new DoubleOption(Text.of("Double Slider"), _ -> Text.empty(), 0.05, 0, 3, null); // todo: widget
	public static final FloatOption floatSlider = new FloatOption(Text.of("Double Slider"), _ -> Text.empty(), 0.1f, 0, 3, null); // todo: widget
	public static final LongOption longSlider = new LongOption(Text.of("Double Slider"), _ -> Text.empty(), 100, 0, 1_000_000, null); // todo: widget
	public static final StringOption textField = new StringOption("Component Option", ""); // todo: widget
	public static final ColorOption colorOption = new ColorOption("Color Option", ""); // todo: widget

	// todo: labels
	public static final ColorOption topColorOption = new ColorOption("Top Color Option", "A Color Controller's Color Picker will appear beneath the color if there is not enough room above it."); // todo: widget
	public static final ColorOption alphaColorOption = new ColorOption("Alpha Color Option", "A Color Picker will also allow you to choose a Color Controller's alpha if it is enabled."); // todo: widget
	public static final ColorOption buttonColorOption = new ColorOption("Button Color Option", "Other controller's buttons are disabled while a color picker is visible!"); // todo: widget
	public static final ColorOption alternativePreviewOutline = new ColorOption("Alternative Color Outline", "This helps users who don't know about the color picker discover it, even if a developer chooses a lighter color"); // todo: widget
	public static final ColorOption anotherAlphaColorOption = new ColorOption("Alpha Color Option 2", "Yay!!!!!!"); // todo: widget

	public static final DoubleOption doubleField = new DoubleOption("Double Field", ""); // todo: widget
	public static final FloatOption floatField = new FloatOption("Float Field", ""); // todo: widget
	public static final IntOption intField = new IntOption("Integer Field", ""); // todo: widget
	public static final LongOption longField = new LongOption("Long Field", ""); // todo: widget

	public static final EnumOption<Alphabet> enumOption = new EnumOption<>("Enum Cycler", "", Alphabet.class); // todo: widget
	public static final CycleOption<String> stringOptions = new CycleOption<>("String Dropdown", "", "Apple", "Banana", "Cherry", "Date"); // todo: widget
	public static final StringOption stringSuggestions = new StringOption("String suggestions", ""); // todo: widget

	//	public static final ItemOption item = new ItemOption("Item Dropdown", ""); // todo: option
	public static final EnumOption<Formatting> formattingOption = new EnumOption<>("Enum Dropdown", "", Formatting.class);

//	public static final ListOption<StringOption> stringList = new ListOption<>("", ""); // todo: option
//	public static final ListOption<IntOption> intList = new ListOption<>("", ""); // todo: option

	public static final BooleanOption groupTestRoot = new BooleanOption("Root Test", "");
	public static final BooleanOption groupTestFirstGroup = new BooleanOption("First Group Test 1", "");
	public static final BooleanOption groupTestFirstGroup2 = new BooleanOption("First Group Test 2", "");
	public static final BooleanOption groupTestSecondGroup = new BooleanOption("Second Group Test", "");

	public enum Alphabet {
		A, B, C
	}


	public static final GooberConfigBuilder CONFIG = GooberConfigBuilder.create("Test GUI", config -> {
		config.category("Control Examples", "Example Category Description", cat -> {
			cat.section("Boolean Controllers", section -> {
				// todo: .flag(OptionFlag.GAME_RESTART)
//				section.option(booleanToggle);
				// todo: .setAvailable on options
//				section.option(customBooleanToggle);
				section.option(tickbox);
			});
			cat.section("Slider Controllers", section -> {
				section.options(intSlider, doubleSlider, floatSlider, longSlider);
			});
			cat.sectionWithOptions("Input Field Controllers", textField, colorOption);
			cat.section("Number Fields", section -> {
				section.option(doubleField);
				section.option(floatField);
				section.option(intField);
				section.option(longField);
			});
			cat.section("Enum Controllers", section -> {
				section.option(enumOption);
			});
			cat.section("Dropdown Controllers", section -> {
				section.option(stringOptions);
				section.option(stringSuggestions);
//				section.option(item);
				section.option(formattingOption);
			});
			cat.section("Options that aren't really options", section -> {
//				section.option(new ButtonOption("Button \"Option\""), opt -> opt.setAvailable(false)); // todo option
//				section.option(new LabelOption(/*...*/)); // todo option
			});
			cat.sectionWithOptions("Minecraft Bindings", "YACL can also bind Minecraft options!", new BooleanOption("Minecraft AutoJump", "You can even bind minecraft options!") {
				@Override
				public void setValue(boolean newValue) {
					MinecraftClient.getInstance().options.getAutoJump().setValue(newValue);
				}

				@Override
				public boolean getValue() {
					return MinecraftClient.getInstance().options.getAutoJump().getValue();
				}

				@Override
				public void resetToDefault() {
					MinecraftClient.getInstance().options.getAutoJump().setValue(false);
				}

				@Override
				public boolean getDefaultValue() {
					return false;
				}
			});
		});
		config.category("List Test", cat -> {
//			cat.option(stringList);
//			cat.option(intList);
//			cat.option(new ListOption<LabelOption>("Useless Label List", List.of(new LabelOption("It's quite impressive that literally every single controller works, without problem.")))); // todo: fuckery
		});
		config.category("Group test", cat -> {
			cat.option(groupTestRoot);
			cat.section("First Group", section -> {
				section.option(groupTestFirstGroup);
				section.option(groupTestFirstGroup2);
			});
			cat.sectionWithOptions("", groupTestSecondGroup); // ???
		});
		config.category("Color Picker Test", cat -> {
			cat.option(topColorOption);
			cat.option(alphaColorOption);
			cat.options(buttonColorOption, alternativePreviewOutline, anotherAlphaColorOption);
		});
		config.category("Category Test", _ -> {
			// todo: label options
		});
		config.category("Category Test", _ -> {
		});
		config.category("Category Test", _ -> {
		});
		config.category("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", _ -> {
		});
		config.category("Category Test", _ -> {
		});
		config.category("Category Test", _ -> {
		});
		config.category("Category Test", _ -> {
		});
		config.category("Placeholder Category", _ -> {
			// todo: custom screens for certain categories
		});
	});
}
