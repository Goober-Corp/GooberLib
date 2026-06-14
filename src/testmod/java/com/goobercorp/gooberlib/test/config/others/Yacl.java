package com.goobercorp.gooberlib.test.config.others;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.category.CategoryBuilder;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.EnumOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.misc.LabelOption;
import com.goobercorp.gooberlib.option.individual.misc.ListOption;
import com.goobercorp.gooberlib.option.individual.primitive.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

@GooberConfig(modId = "java")
public class Yacl {
	public static final BooleanOption booleanToggle = new BooleanOption(Component.literal("Boolean Toggle"), _ -> Component.empty()
			.append(Component.literal("a").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("a")))))
			.append(Component.literal("b").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("b")))))
			.append(Component.literal("c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("c")))))
			.append(Component.literal("e").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("e")))))
			.append(Component.literal("click me").withStyle(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://isxander.dev"))))), false, WidgetProviders.booleanToggleWidget()
	);
	public static final BooleanOption customBooleanToggle = new BooleanOption("Custom Boolean Toggle", "You can customize controllers like so! YACL is truly infinitely customizable! This tooltip is long in order to demonstrate the cool, smooth scrolling of these descriptions. Did you know, they are also super clickable?! I know, cool right, YACL 3.x really is amazing.", WidgetProviders.booleanToggleWidget("I am true", "I am false"));
	public static final BooleanOption tickbox = new BooleanOption("Tick Box", "There are even alternate methods of displaying the same data type!");

	public static final IntOption intSlider = new IntOption("Int Slider", _ -> "", 1, 0, 3, null);
	public static final DoubleOption doubleSlider = new DoubleOption("Double Slider", _ -> "", 0.05, 0, 3, null);
	public static final FloatOption floatSlider = new FloatOption("Double Slider", _ -> "", 0.1f, 0, 3, null);
	public static final LongOption longSlider = new LongOption("Double Slider", _ -> "", 100, 0, 1_000_000, null);
	public static final StringOption ComponentField = new StringOption("Component Option", "");
	public static final ColorOption colorOption = new ColorOption("Color Option", ""); // todo: widget

	public static final ColorOption topColorOption = new ColorOption("Top Color Option", "A Color Controller's Color Picker will appear beneath the color if there is not enough room above it."); // todo: widget
	public static final ColorOption alphaColorOption = new ColorOption("Alpha Color Option", "A Color Picker will also allow you to choose a Color Controller's alpha if it is enabled."); // todo: widget
	public static final ColorOption buttonColorOption = new ColorOption("Button Color Option", "Other controller's buttons are disabled while a color picker is visible!"); // todo: widget
	public static final ColorOption alternativePreviewOutline = new ColorOption("Alternative Color Outline", "This helps users who don't know about the color picker discover it, even if a developer chooses a lighter color"); // todo: widget
	public static final ColorOption anotherAlphaColorOption = new ColorOption("Alpha Color Option 2", "Yay!!!!!!"); // todo: widget

	public static final DoubleOption doubleField = new DoubleOption("Double Field", "", WidgetProviders.numberField());
	public static final FloatOption floatField = new FloatOption("Float Field", "", WidgetProviders.numberField());
	public static final IntOption intField = new IntOption("Integer Field", "", WidgetProviders.numberField());
	public static final LongOption longField = new LongOption("Long Field", "", WidgetProviders.numberField());

	public static final EnumOption<Alphabet> enumOption = new EnumOption<>("Enum Cycler", "", Alphabet.class);
	public static final CycleOption<String> stringOptions = new CycleOption<>("String Dropdown", "", "Apple", "Banana", "Cherry", "Date");
	public static final StringOption stringSuggestions = new StringOption("String suggestions", "", WidgetProviders.stringField(s -> List.of(s + "1", s + "2", s + "3"))); // todo: widget

	//	public static final ItemOption item = new ItemOption("Item Dropdown", ""); // todo: option
	public static final EnumOption<ChatFormatting> formattingOption = new EnumOption<>("Enum Dropdown", "", ChatFormatting.class);

	public static final ListOption<StringOption> stringList = new ListOption<>("String List", List.of(), () -> new StringOption("", "")); //todo: widget
	public static final ListOption<IntOption> intList = new ListOption<>("Slider List", List.of(), () -> new IntOption("", _ -> "", 0, 0, 10, null)); //todo: widget

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
				section.option(booleanToggle);
				// todo: .setAvailable on options
				section.option(customBooleanToggle);
				section.option(tickbox);
			});
			cat.section("Slider Controllers", section -> {
				section.options(intSlider, doubleSlider, floatSlider, longSlider);
			});
			cat.sectionWithOptions("Input Field Controllers", ComponentField, colorOption);
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
				section.option(new ButtonOption("Button \"Option\"", /*opt -> opt.setAvailable(false)*/() -> System.out.println("Click!")));
				section.option(new LabelOption(Component.empty()
						.append(Component.literal("a").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("a")))))
						.append(Component.literal("b").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("b")))))
						.append(Component.literal("c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("c")))))
						.append(Component.literal("e").withStyle(style -> style.withHoverEvent(new HoverEvent.ShowText(Component.literal("e")))))
						.append(Component.literal("click me").withStyle(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://isxander.dev")))))));
			});
			cat.sectionWithOptions("Minecraft Bindings", "YACL can also bind Minecraft options!", new BooleanOption("Minecraft AutoJump", "You can even bind minecraft options!") {
				@Override
				public void setValue(boolean newValue) {
					Minecraft.getInstance().options.autoJump().set(newValue);
				}

				@Override
				public boolean getValue() {
					return Minecraft.getInstance().options.autoJump().get();
				}

				@Override
				public void resetToDefault() {
					Minecraft.getInstance().options.autoJump().set(false);
				}

				@Override
				public boolean getDefaultValue() {
					return false;
				}
			});
		});
		config.category("List Test", cat -> {
			cat.option(stringList);
			cat.option(intList);
			cat.option(new ListOption<>("Useless Label List", List.of(new LabelOption("It's quite impressive that literally every single controller works, without problem.")), () -> new LabelOption("Initial label")));
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
		Consumer<CategoryBuilder> testCategoryMaker = cat -> cat.option(new LabelOption("This is a test category!"));

		config.category("Category Test", cat -> cat.option(new LabelOption("This is a test category!")));
		config.category("Category Test", cat -> cat.option(new LabelOption("This is a test category!")));
		config.category("Category Test", testCategoryMaker);
		config.category("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", testCategoryMaker);
		for (int i = 0; i < 3; i++) {
			config.category("Category Test", testCategoryMaker);
		}
		config.category("Placeholder Category", _ -> {
			// todo: custom screens (or i guess a custom CategoryWidget?) for certain categories
		});
	});
}
