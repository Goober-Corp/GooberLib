package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EvilerColoredQuadGuiElementRenderState(
        RenderPipeline pipeline,
        TextureSetup textureSetup,
        Matrix3x2fc matrix,
        float x,
        float y,
        float x2,
        float y2,
        int col1,
        int col2,
        int col3,
        int col4,
        @Nullable ScreenRect comp_4069,
        @Nullable ScreenRect comp_4274
) implements SimpleGuiElementRenderState {
    public EvilerColoredQuadGuiElementRenderState(
            RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2fc matrix3x2fc, float i, float j, float k, float l, int m, int n, int o, int p, @Nullable ScreenRect screenRect
    ) {
        this(renderPipeline, textureSetup, matrix3x2fc, i, j, k, l, m, n, o, p, screenRect, createBounds((int) i, (int) j, (int) k, (int) l, matrix3x2fc, screenRect));
    }

    @Override
    public void setupVertices(VertexConsumer vertexConsumer) {
        //top left
        vertexConsumer.vertex(this.matrix(), this.x(), (y() + y2()) / 2F).color(col1);
        //bottom left
        vertexConsumer.vertex(this.matrix(), this.x(), (y() + y2()) / 2F).color(this.col2());
        //bottom right
        vertexConsumer.vertex(this.matrix(), this.x2(), this.y2()).color(col3);
        //top right
        vertexConsumer.vertex(this.matrix(), this.x2(), this.y()).color(this.col4());
    }

    @Override
    public RenderPipeline comp_4055() {
        return pipeline;
    }

    @Override
    public TextureSetup comp_4056() {
        return textureSetup;
    }

    @Nullable
    private static ScreenRect createBounds(int i, int j, int k, int l, Matrix3x2fc matrix3x2fc, @Nullable ScreenRect screenRect) {
        ScreenRect screenRect2 = new ScreenRect(i, j, k - i, l - j).transformEachVertex(matrix3x2fc);
        return screenRect != null ? screenRect.intersection(screenRect2) : screenRect2;
    }
}
