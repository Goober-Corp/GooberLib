package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.gui.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.gui.GroupTextWidget;
import com.goobercorp.gooberlib.gui.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.ScrollTweener;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class GooberScreen extends Screen {
    private static final int VERTICAL_PADDING = 32;
    private static final int CHILD_INSET = 16;
    private final BuiltConfig config;
    private final Screen parent;
    private Text descriptionText = Text.of("");
    private float descriptionAnimationProgress = 0;
    private float categoryHoverProgress = 1;
    private float screenCategoryAnimationState = 0;
    private EvilTabNavigationWidget tabNavigationWidget;
    private int tabHoldTicks = 20;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private double scrollProgress = 0;
    private final ScrollTweener scrollTweener = new ScrollTweener(() -> scrollProgress, writeTo -> scrollProgress = writeTo, -1000, 0);
    private int lastScrollTicks = 0;
    private final Tweener categoryTweener = new Tweener(() -> screenCategoryAnimationState);
    private final HashMap<OptionHolder, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
    private final Tab[] tabs;
    private final int[] heights;
    private boolean animateHoverDescription = false;
    public ScreenRect badbadbad = new ScreenRect(0, 0, 0, 0);

    private final String modId;

    public GooberScreen(BuiltConfig config, Screen parent, String modId) {
        super(config.title());
        this.config = config;
        this.parent = parent;
        this.modId = modId;

        this.tabs = new GridScreenTab[config.categories().size()];
        for (int i = 0; i < config.categories().size(); i++) {
            tabs[i] = new GridScreenTab(config.categories().get(i).metadata().name());
        }
        heights = new int[tabs.length];
    }

    @Override
    protected void init() {
        evilLayout.clear();

        this.tabNavigationWidget = this.addSelectableChild(EvilTabNavigationWidget.builder(tabManager, width)
                .tabs(tabs)
                .build());
        this.tabNavigationWidget.init();
        tabNavigationWidget.selectTab(0, false);

        for (ConfigCategory c : config.categories()) {
            int x = MinecraftClient.getInstance().getWindow().getScaledWidth() * (config.categories().indexOf(c));
            int y = VERTICAL_PADDING;
            for (OptionHolder o : c.elements()) {
                if (o instanceof ConfigSection(
                        Metadata metadata,
                        List<OptionContext<?>> childOptionContexts
                )) {
                    GroupTextWidget t = new GroupTextWidget(
                            metadata.name(),
                            textRenderer
                    );
                    PrecisePositionWidgetWrapper<GroupTextWidget> widgetWrapper = new PrecisePositionWidgetWrapper<>(t, x + ((double) MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - (double) textRenderer.getWidth(metadata.name()) / 2, y, metadata.description());
                    evilLayout.put(o, widgetWrapper);
                    if (new ScreenRect((int) widgetWrapper.getRealX(), (int) widgetWrapper.getRealY(), widgetWrapper.getWrapped().getRight(), widgetWrapper.getWrapped().getBottom()).overlaps(new ScreenRect(0, 0, width, height))) {
                        t.renderProgress = 1;
                    }
                    addDrawableChild(widgetWrapper);
                    y += VERTICAL_PADDING;
                    for (OptionContext<?> yeah : childOptionContexts) {
                        y += addOptionWithChildren(yeah, y, x + CHILD_INSET);
                    }
                } else {
                    y += addOptionWithChildren((OptionContext<?>) o, y, x + CHILD_INSET);
                }
                // TODO: maybe store scroll height for each category?
                heights[config.categories().indexOf(c)] = Math.max(y - height, 0);
            }
        }
        //to prevent weirdness on resize
        setWidgetOffsets();
    }

    private void setWidgetOffsets() {
        for (PrecisePositionWidgetWrapper<?> entry : evilLayout.values()) {

//            if (scrollTweener.get() < scrollTweener.min) {
//                //TODO: this is kinda busted???
//                /*entry.setOffsetY(scrollTweener.get() * ((scrollTweener.get() - scrollTweener.min) / (entry.getY() - scrollTweener.min)));*/
//                entry.setOffsetY(scrollTweener.get() - (scrollTweener.get() * Math.abs(((scrollTweener.get() - scrollTweener.min) / (-entry.getY())))));
//            } else if (scrollTweener.get() > scrollTweener.max) {
//                entry.setOffsetY(scrollTweener.get() + (scrollTweener.get() * Math.abs(((scrollTweener.get() - scrollTweener.max) / entry.getY()))));
//                /*entry.setOffsetY(scrollTweener.get() * (scrollTweener.get() / entry.getY()));*/
//            } else {
//            }
            entry.setOffsetY(scrollTweener.get());
            entry.setOffsetX(-width * categoryTweener.get());
        }
    }

    private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x) {
        int addY = 0;
        Option<?> option = optionContext.option();
        ClickableWidget widget = option.getWidgetProvider().makeWidget(option, 0, 0, 125, VERTICAL_PADDING / 2.0);

        PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x, y + addY, option.description());
        this.addDrawableChild(pw);
        evilLayout.put(optionContext, pw);
        addY += VERTICAL_PADDING;

        for (OptionContext<?> child : optionContext.childOptions()) {
            addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
        }

        return addY;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        updateTweeners();
        setWidgetOffsets();
        evilLayout.values().forEach(entry -> entry.render(drawContext, mouseX, mouseY, tickDelta));
        drawLines(drawContext);

        super.render(drawContext, mouseX, mouseY, tickDelta);

        for (PrecisePositionWidgetWrapper<?> yeah : evilLayout.values()) {
            if (yeah.isMouseOver(mouseX, mouseY)) {
                descriptionText = yeah.getHoverMessage();
                animateHoverDescription = true;
                break;
            }
            animateHoverDescription = false;
        }

        if (tabNavigationWidget.isMouseOver(mouseX, mouseY)) {
            tabHoldTicks = 10;
        }
        categoryHoverProgress = (float) ease(categoryHoverProgress, tabHoldTicks > 0 ? 1 : 0, 15);
        int yeah = tabNavigationWidget.getCurrentTabIndex();
        screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);


        descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animateHoverDescription ? 1 : 0, 15);
        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));

        newMatrixScope(drawContext, matrix3x2fStack -> {
            matrix3x2fStack.translate(0, -26 * (1 - categoryHoverProgress));
            tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
        });
        scrollTweener.min = -heights[tabNavigationWidget.getCurrentTabIndex()];
    }

    @Override
    public void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        newMatrixScope(drawContext, stack -> {
            stack.translate(-mouseX * 0.1F, -mouseY * 0.1F);
            stack.translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
            stack.scale(2.5F, 2.5F);
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
        });

        drawLines(drawContext);

        setWidgetOffsets();
        evilLayout.values().forEach(entry -> entry.render(drawContext, mouseX, mouseY, tickDelta));

        newMatrixScope(drawContext, stack -> {
            stack.translate(0, -26 * (1 - categoryHoverProgress));
            tabNavigationWidget.renderForBackgroundLayer(drawContext);
        });

        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));
        drawContext.createNewRootLayer();

        super.renderBackground(drawContext, mouseX, mouseY, tickDelta);
    }

    private void forEachOption(Consumer<OptionHolder> consumer) {
        //run a function for every top-level option, including standalone options
        for (ConfigCategory c : config.categories()) {
            for (OptionHolder o : c.elements()) {
                if (o instanceof ConfigSection) {
                    for (OptionHolder opt : o.childOptions()) {
                        consumer.accept(opt);
                    }
                } else {
                    if (!o.childOptions().isEmpty()) {
                        consumer.accept(o);
                    }
                }
            }
        }
    }

    private void drawLines(DrawContext drawContext) {
        forEachOption(optionHolderV3 -> drawLinesForOption(drawContext, optionHolderV3));
    }

    private void drawLinesForOption(DrawContext drawContext, OptionHolder o) {
        if (o.childOptions().isEmpty()) return;
        PrecisePositionWidgetWrapper<?> mainWidget = evilLayout.get(o);
        PrecisePositionWidgetWrapper<?> lastChildWidget = evilLayout.get(o.childOptions().getLast());
        RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight(), (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F) + 1, 0x80000000);
        RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1, (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F), -1);
        for (OptionHolder opt : o.childOptions()) {
            PrecisePositionWidgetWrapper<?> optionWidget = evilLayout.get(opt);
            RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) evilLayout.get(opt).getRealX(), (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + 1, 0x80000000);
            RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) evilLayout.get(opt).getRealX() - 1, (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F, -1);
            drawLinesForOption(drawContext, opt);
        }
    }

    private void updateTweeners() {
        this.scrollTweener.update();
        this.categoryTweener.update();
    }

    @Override
    public void tick() {
        tabNavigationWidget.tick();
        tabHoldTicks = Math.clamp(tabHoldTicks - 1, 0, 100);
        lastScrollTicks++;
        if (lastScrollTicks > 2) {
            scrollTweener.setInteractionState(false);
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
        GooberLibApi.save(modId, config);
    }

    @Override
    public void resize(int i, int j) {
        int selectedTab = tabNavigationWidget.getCurrentTabIndex();
        super.resize(i, j);
        tabNavigationWidget.selectTab(selectedTab == -1 ? 0 : selectedTab, false);
    }

    @Override
    public boolean mouseClicked(Click click, boolean bl) {
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean mouseDragged(Click click, double d, double e) {
        if (super.mouseDragged(click, d, e)) return true;

        if (click.button() == 0 && !tabNavigationWidget.isMouseOver(d, e)) {
            lastScrollTicks = 0;
            scrollTweener.setInteractionState(true);
            if ((scrollProgress < scrollTweener.min && e < 0) || (scrollProgress > scrollTweener.max && e > 0)) {
                scrollProgress += e * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
            } else {
                scrollProgress += e;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        lastScrollTicks = 0;
        scrollTweener.setInteractionState(true);
        if (!tabNavigationWidget.isMouseOver(d, e)) {
            if (scrollProgress < scrollTweener.min || scrollProgress > scrollTweener.max) {
                scrollProgress += g * 15 * Math.min(1 / Math.abs(scrollProgress - Math.clamp(scrollProgress, scrollTweener.min, scrollTweener.max)), 1);
            } else {
                scrollProgress += g * 15;
            }
        }
        return super.mouseScrolled(d, e, f, g);
    }
}
