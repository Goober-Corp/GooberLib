package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GooberScreen extends Screen {
    private final BuiltConfig config;
    private Screen parent;

    public GooberScreen(BuiltConfig config, Screen parent){
        super(config.title());
        this.config = config;
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void renderBackground(DrawContext drawContext, int i, int j, float f) {
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(-i * 0.1F, -j * 0.1F);
        drawContext.getMatrices().translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2F - 100, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2F - 100);
        drawContext.getMatrices().scale(2.5F, 2.5F);
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib","textures/him.png"), 0,0, 100, 100, 100, 100, 100, 100);
        drawContext.getMatrices().popMatrix();
        drawContext.createNewRootLayer();
        super.renderBackground(drawContext, i, j, f);

//        this.applyBlur(drawContext);
    }
}
