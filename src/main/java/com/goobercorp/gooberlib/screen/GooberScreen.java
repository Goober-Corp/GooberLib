package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.mixin.ClickableWidgetAcessor;
import com.goobercorp.gooberlib.mixin.ScreenAccessor;
import com.goobercorp.gooberlib.mixin.TooltipAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class GooberScreen extends Screen {
    private final BuiltConfig config;
    private final Screen parent;
    private Text descriptionText = Text.of("");
    private float descriptionAnimationProgress = 0;
    private float categoryHoverProgress = 0;
    private float screenCategoryAnimationState = 0;
    private TabNavigationWidget tabNavigationWidget;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private Tab[] tabs;

    public GooberScreen(BuiltConfig config, Screen parent) {
        super(config.title());
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void init() {
		this.tabs = new GridScreenTab[config.categories().size()];
		for (int i = 0; i < config.categories().size(); i++) {
			tabs[i] = new GridScreenTab(config.categories().get(i).metadata().name());
		}

        this.tabNavigationWidget = this.addSelectableChild(TabNavigationWidget.builder(tabManager, width)
                .tabs(tabs)
                .build());
		this.tabNavigationWidget.selectTab(0, false);
        this.tabNavigationWidget.init();

        // padding
        int y = 25;
        for (ConfigCategory c : config.categories()) {
            for (Object o : c.elements()) {
                if (o instanceof ConfigSection configSection) {
                    TextWidget t = new TextWidget(
							((MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - textRenderer.getWidth(configSection.metadata().name()) / 2) * (config.categories().indexOf(c) + 1),
							y,
							0,
							textRenderer.fontHeight,
							configSection.metadata().name(),
							textRenderer
					);
                    t.setMaxWidth(Integer.MAX_VALUE);
                    if (configSection.metadata().description() != null) {
                        t.setTooltip(Tooltip.of(configSection.metadata().description()));
                    }
                    this.addDrawable(t);
                    y += 25;
                }
            }
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    public static double ease(double start, double end, float speed) {
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-width * screenCategoryAnimationState, 0);
        super.render(drawContext, i, j, f);
        drawContext.getMatrices().popMatrix();

        AtomicBoolean animate = new AtomicBoolean(false);
        ((ScreenAccessor) this).drawables().forEach(drawable -> {
            if (drawable instanceof TextWidget textWidget) {
				var accessor = (ClickableWidgetAcessor) textWidget;
                if (textWidget.isHovered()) {
                    animate.set(true);
					var accessor2 = (TooltipAccessor) accessor.tooltip().getTooltip();
					descriptionText = accessor2 == null ? Text.of("") : accessor2.content();
                }
            }
        });

        categoryHoverProgress = (float) ease(categoryHoverProgress, tabNavigationWidget.isMouseOver(i, j) ? 1 : 0, 15);
        int yeah = ArrayUtils.indexOf(tabs, tabManager.getCurrentTab());
        screenCategoryAnimationState = (float) ease(screenCategoryAnimationState, yeah == -1 ? 0 : yeah, 15);

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0, -26 * (1 - categoryHoverProgress));
        tabNavigationWidget.render(drawContext, i, j, f);
        drawContext.getMatrices().popMatrix();

		descriptionAnimationProgress = (float) ease(descriptionAnimationProgress, animate.get() ? 1 : 0, 15);
        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));
    }

    @Override
    public void renderBackground(DrawContext drawContext, int i, int j, float f) {
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-i * 0.1F, -j * 0.1F);
        drawContext.getMatrices().translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
        drawContext.getMatrices().scale(2.5F, 2.5F);
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "textures/him.png"), 0, 0, 100, 100, 100, 100, 100, 100);
        drawContext.getMatrices().popMatrix();

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-width * screenCategoryAnimationState, 0);
        ((ScreenAccessor) this).drawables().forEach(drawable -> {
            if (drawable instanceof TextWidget) {
                drawable.render(drawContext, i, j, f);
            }
        });
        drawContext.getMatrices().popMatrix();

        drawContext.createNewRootLayer();
        super.renderBackground(drawContext, i, j, f);
    }
}
