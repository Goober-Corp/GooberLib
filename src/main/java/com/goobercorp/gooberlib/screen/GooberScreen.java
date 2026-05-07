package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.ConfigOption;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.v2.OptionHolder;
import com.goobercorp.gooberlib.gui.GroupTextWidget;
import com.goobercorp.gooberlib.gui.EvilTabNavigationWidget;
import com.goobercorp.gooberlib.mixin.ClickableWidgetAcessor;
import com.goobercorp.gooberlib.mixin.ScreenAccessor;
import com.goobercorp.gooberlib.mixin.TooltipAccessor;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.joml.Vector2i;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.goobercorp.gooberlib.util.RenderUtils.drawHorizontalLine;
import static com.goobercorp.gooberlib.util.RenderUtils.ease;

public class GooberScreen extends Screen {
    private static final int VERTICAL_PADDING = 32;
    private static final int HORIZONTAL_PADDING = 32;
    private static final int CHILD_INSET = 16;
    private final BuiltConfig config;
    private final Screen parent;
    private Text descriptionText = Text.of("");
    private float descriptionAnimationProgress = 0;
    private float categoryHoverProgress = 1;
    private float screenCategoryAnimationState = 0;
    private EvilTabNavigationWidget tabNavigationWidget;
    private int firstOpenTabAnimationTicks = 20;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private double scrollProgress = 0;
    private final Tweener scrollTweener = new Tweener(() -> scrollProgress);
    private Tab[] tabs;
    String modid;

	public GooberScreen(BuiltConfig config, Screen parent, String modid) {
        super(config.title());
        this.config = config;
        this.parent = parent;
        this.modid = modid;
    }

    @Override
    public void tick() {
        firstOpenTabAnimationTicks -= 1;
    }

    private int addOptionWithChildren(ConfigOption option, int y, int x) {
        int addY = 0;
        ClickableWidget widget;
        if (option.type().getTypeName().equals("int")) {
            widget = new SliderWidget(x, y, 125, 16, option.metadata().name(), ((int) option.getter().get()) / 25F) {
                {
                    this.updateMessage();
                }

                @Override
                protected void updateMessage() {
                    this.setMessage(Text.of(option.metadata().name().getString() + ": " + MathHelper.floor(MathHelper.clampedLerp(this.value, 0, 25))));
                }

                @Override
                protected void applyValue() {
                    ((Consumer<Integer>) option.setter()).accept(MathHelper.floor(MathHelper.clampedLerp(this.value, 0, 25)));
                }
            };
        } else {
            widget = new TextWidget(
                    option.metadata().name(),
                    textRenderer
            );
        }
        widget.setX(x);
        widget.setY(y + addY);
        this.addDrawableChild(widget);
        addY += VERTICAL_PADDING;

        for (ConfigOption child : option.childOptions()) {
            addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
        }

        return addY;
    }

    @Override
    protected void init() {
        this.tabs = new GridScreenTab[config.categories().size()];
        for (int i = 0; i < config.categories().size(); i++) {
            tabs[i] = new GridScreenTab(config.categories().get(i).metadata().name());
        }
        this.tabNavigationWidget = this.addSelectableChild(EvilTabNavigationWidget.builder(tabManager, width)
                .tabs(tabs)
                .build());
        this.tabNavigationWidget.selectTab(0, false);
        this.tabNavigationWidget.init();

        for (ConfigCategory c : config.categories()) {
            int x = (config.categories().indexOf(c) == 0 ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth() * (config.categories().indexOf(c)));
            int y = VERTICAL_PADDING;
            for (OptionHolder o : c.elements()) {
                if (o instanceof ConfigSection configSection) {
                    GroupTextWidget t = new GroupTextWidget(
                            configSection.metadata().name(),
                            textRenderer
                    );
                    t.setX(x + (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - textRenderer.getWidth(configSection.metadata().name()) / 2);
                    t.setY(y);
                    if (configSection.metadata().description() != null) {
                        t.setTooltip(Tooltip.of(configSection.metadata().description()));
                    }
                    this.addDrawable(t);
                    y += VERTICAL_PADDING;
                    for (ConfigOption yeah : o.childOptions()) {
                        y += addOptionWithChildren(yeah, y, x + CHILD_INSET);
                    }
                } else {
                    y += addOptionWithChildren((ConfigOption) o, y, x + CHILD_INSET);
                }
            }
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
        GooberLibApi.save(modid, config);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        scrollProgress = MathHelper.clamp(scrollProgress + g * 15, -1000, 0);
        return super.mouseScrolled(d, e, f, g);
    }
//    @Override
//    public boolean mouseClicked(Click click, boolean bl)
//    {
//        Click c = new Click(click.comp_4798(), click.comp_4799() - scrollProgress, click.comp_4800());
//        return super.mouseClicked(c, bl);
//    }
//TODO: come up with a better way to move stuff around rather than shifting matrices

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
//        mouseY -= (int) scrollProgress;
//        mouseX -= (int) (-width * screenCategoryAnimationState);
        //TODO: do something about tooltips not lining up
		this.scrollTweener.update();
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-width * screenCategoryAnimationState, (float) scrollTweener.get());
        drawLines(drawContext);
        super.render(drawContext, mouseX - (int) (-width * screenCategoryAnimationState), (int) (mouseY - scrollProgress), tickDelta);
        drawContext.getMatrices().popMatrix();


        AtomicBoolean animate = new AtomicBoolean(false);
        ((ScreenAccessor) this).drawables().forEach(drawable -> {
            if (drawable instanceof AbstractTextWidget textWidget) {
                var accessor = (ClickableWidgetAcessor) textWidget;
                if (textWidget.isHovered()) {
                    animate.set(true);
                    var accessor2 = (TooltipAccessor) accessor.tooltip().getTooltip();
                    descriptionText = accessor2 == null ? Text.of("") : accessor2.content();
                }
            }
        });


        categoryHoverProgress = (float) ease(categoryHoverProgress, tabNavigationWidget.isMouseOver(mouseX, mouseY) || firstOpenTabAnimationTicks > 0 ? 1 : 0, 15);
        int yeah = ArrayUtils.indexOf(tabs, tabManager.getCurrentTab());
        screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0, -26 * (1 - categoryHoverProgress));
        tabNavigationWidget.render(drawContext, mouseX, mouseY, tickDelta);
        drawContext.getMatrices().popMatrix();

        descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animate.get() ? 1 : 0, 15);
        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));
    }

    private void drawLines(DrawContext drawContext) {
        for (ConfigCategory c : config.categories()) {
            int x = (config.categories().indexOf(c) == 0 ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth() * (config.categories().indexOf(c))) + (int) (HORIZONTAL_PADDING * 0.75F);
            Vector2i vector2i = new Vector2i(x, VERTICAL_PADDING);
            for (OptionHolder o : c.elements()) {
                if (!(o instanceof ConfigSection)) {
                    if (!o.childOptions().isEmpty()) {
                        vector2i.add(drawLinesForOption((ConfigOption) o, drawContext, vector2i));
                    }
                    vector2i.x = x;
                } else {
                    for (ConfigOption opt : o.childOptions()) {
                        vector2i.add(drawLinesForOption(opt, drawContext, vector2i));
                    }
                    vector2i.x = x;
                }
            }
        }
    }

    private Vector2i drawLinesForOption(ConfigOption option, DrawContext drawContext, Vector2i vec) {
        int returnY = (VERTICAL_PADDING * option.childOptions().size());
        RenderUtils.drawVerticalLine(drawContext, vec.x, vec.y + 8, vec.y + (VERTICAL_PADDING * option.childOptions().size()) + 8, 0xFFFFFFFF);
        RenderUtils.drawVerticalLine(drawContext, vec.x + 1, vec.y + 9, vec.y + (VERTICAL_PADDING * option.childOptions().size()) + 9, 0xFF3e3e3e);
        //I fucked up.
        for (ConfigOption opt : option.childOptions()) {
            int x2 = vec.x + CHILD_INSET;
            int inset = VERTICAL_PADDING * (option.childOptions().indexOf(opt) + 1);
            returnY += inset;
            drawHorizontalLine(drawContext, vec.x, x2, vec.y + 8 + inset, 0xFFFFFFFF);
            drawHorizontalLine(drawContext, vec.x + 1, x2 + 1, vec.y + 8 + inset + 1, 0xFF3e3e3e);
            drawLinesForOption(opt, drawContext, new Vector2i(x2, vec.y + inset));
        }
        return new Vector2i(vec.x, returnY);
    }

    @Override
    public void renderBackground(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        //TODO: make this better
        mouseY -= (int) scrollProgress;
        mouseX -= (int) (-width * screenCategoryAnimationState);
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-mouseX * 0.1F, -mouseY * 0.1F);
        drawContext.getMatrices().translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
        drawContext.getMatrices().scale(2.5F, 2.5F);
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
        drawContext.getMatrices().popMatrix();

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-width * screenCategoryAnimationState, (float) scrollTweener.get());
        drawLines(drawContext);
        int finalX = mouseX;
        int finalY = mouseY;
        ((ScreenAccessor) this).drawables().forEach(drawable -> {
            if (drawable instanceof AbstractTextWidget) {
                drawable.render(drawContext, finalX, finalY, tickDelta);
            }
        });
        drawContext.getMatrices().popMatrix();

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0, -26 * (1 - categoryHoverProgress));
        tabNavigationWidget.renderForBackgroundLayer(drawContext);
        drawContext.getMatrices().popMatrix();

        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));

        drawContext.createNewRootLayer();
        super.renderBackground(drawContext, mouseX, mouseY, tickDelta);
    }

}
