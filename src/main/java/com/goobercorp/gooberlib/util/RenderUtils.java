package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

import java.util.function.Consumer;

import static java.lang.Math.exp;
import static net.minecraft.client.gl.RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET;

public class RenderUtils {
    public static double ease(double start, double end, float speed) {
        var dt = 1.0F / MinecraftClient.getInstance().getCurrentFps();
        return start + (end - start) * (1 - exp(-dt * speed));
    }

    public static final RenderPipeline.Snippet GUI_SNIPPET = RenderPipeline.builder(TRANSFORMS_AND_PROJECTION_SNIPPET)
            .withVertexShader("core/gui")
            .withFragmentShader("core/gui")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .buildSnippet();

    public static void drawHorizontalLine(DrawContext context, float x1, float x2, float y, int col) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEvil(context, x1, y, x2 + 1, y + 1, col);
    }

    public static void drawHorizontalLine(DrawContext context, float x1, float x2, float y, int col, int col2) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEvil(context, x1, y, x2 + 1, y + 1, col, col2);
    }

    public static void drawThinningHorizontalLine(DrawContext context, float x1, float x2, float y, int col, int col2) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEviler(context, x1, y - 10F, x2 + 1, y + 10F, col, col2);
    }

    public static void drawHorizontalLine(DrawContext context, float x1, float x2, float y, int col, int col2, int col3, int col4) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEvil(context, x1, y, x2 + 1, y + 1, col, col2, col3, col4);
    }

    public static void drawVerticalLine(DrawContext context, float x, float y1, float y2, int col) {
        if (y2 < y1) {
            float m = y1;
            y1 = y2;
            y2 = m;
        }

        fillEvil(context, x, y1 + 1, x + 1, y2, col);
    }

    public static void fillEvil(DrawContext context, float x, float y, float x2, float y2, int col) {
        context.state
                .addSimpleElement(
                        new EvilColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col, col, col, context.scissorStack.peekLast()
                        )
                );
    }

    public static void fillEvil(DrawContext context, float x, float y, float x2, float y2, int col, int col2) {
        context.state
                .addSimpleElement(
                        new EvilColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col, col2, col2, context.scissorStack.peekLast()
                        )
                );
    }

    public static void fillEviler(DrawContext context, float x, float y, float x2, float y2, int col, int col2) {
        context.state
                .addSimpleElement(
                        new EvilerColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col, col2, col2, context.scissorStack.peekLast()
                        )
                );
    }

    public static void fillEvil(DrawContext context, float x, float y, float x2, float y2, int col, int col2, int col3, int col4) {
        context.state
                .addSimpleElement(
                        new EvilColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col2, col3, col4, context.scissorStack.peekLast()
                        )
                );
    }

    public static void drawBoxOutline(DrawContext context, float x, float y, float x2, float y2, int col) {
        drawHorizontalLine(context, x, x2, y, col);
        drawHorizontalLine(context, x, x2, y2, col);

        drawVerticalLine(context, x, y, y2, col);
        drawVerticalLine(context, x2, y, y2, col);
    }

    public static void newMatrixScope(DrawContext context, Consumer<Matrix3x2fStack> function) {
        context.getMatrices().pushMatrix();
        function.accept(context.getMatrices());
        context.getMatrices().popMatrix();
    }

    public static boolean isInBounds(double mouseX, double mouseY, ScreenRect rect) {
        return mouseX >= rect.getLeft() && mouseY >= rect.getTop() && mouseX < rect.getRight() && mouseY < rect.getBottom();
    }
}
