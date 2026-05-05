package com.goobercorp.gooberlib.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;

public class RenderUtils {
    public static double ease(double start, double end, float speed) {
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }

    public static void drawHorizontalLine(DrawContext context, float x1, float x2, float y, int col) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEvil(context, x1, y, x2 + 1, y + 1, col);
    }public static void drawHorizontalLine(DrawContext context, float x1, float x2, float y, int col, int col2) {
        if (x2 < x1) {
            float m = x1;
            x1 = x2;
            x2 = m;
        }

        fillEvil(context, x1, y, x2 + 1, y + 1, col, col2);
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
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col, context.scissorStack.peekLast()
                        )
                );
    }
    public static void fillEvil(DrawContext context, float x, float y, float x2, float y2, int col, int col2) {
        context.state
                .addSimpleElement(
                        new EvilColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col2, context.scissorStack.peekLast()
                        )
                );
    }
}
