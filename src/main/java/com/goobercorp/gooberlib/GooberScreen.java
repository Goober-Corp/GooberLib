package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.mixin.ClickableWidgetAcessor;
import com.goobercorp.gooberlib.mixin.ScreenMixin;
import com.goobercorp.gooberlib.mixin.TooltipAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class GooberScreen extends Screen {
    private final BuiltConfig config;
    private final Screen parent;
    private Text descriptionText = Text.of("");
    private float descriptionAnimationProgress = 0;

    public GooberScreen(BuiltConfig config, Screen parent) {
        super(config.title());
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // padding
        int y = 25;
        //TODO: tabs
        for (ConfigCategory c : config.categories()) {
            for (Object o : c.elements()) {
                if (o instanceof ConfigSection) {
                    TextWidget t = new TextWidget((MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - textRenderer.getWidth(((ConfigSection) o).metadata().name()) / 2, y, 100, textRenderer.fontHeight, ((ConfigSection) o).metadata().name(), textRenderer);
                    //check for null
                    if (((ConfigSection) o).metadata().description() != null) {
                        t.setTooltip(Tooltip.of(((ConfigSection) o).metadata().description()));
                    }
                    addDrawable(t);
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
        super.render(drawContext, i, j, f);
        AtomicBoolean animate = new AtomicBoolean(false);
        ((ScreenMixin) this).drawables().forEach(drawable -> {
            if (drawable instanceof TextWidget) {
                if (((TextWidget) drawable).isHovered()) {
                    animate.set(true);
                    descriptionText = ((TooltipAccessor) ((ClickableWidgetAcessor) drawable).tooltip().getTooltip()).content();
                }
            }
        });
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
        drawContext.drawCenteredTextWithShadow(textRenderer, descriptionText, drawContext.getScaledWindowWidth() / 2, (int) (drawContext.getScaledWindowHeight() * (1.05F + (-0.1 * descriptionAnimationProgress))), ColorHelper.getWhite(descriptionAnimationProgress));
        drawContext.createNewRootLayer();
        super.renderBackground(drawContext, i, j, f);
    }

}
