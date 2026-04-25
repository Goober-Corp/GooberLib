package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.screen.EvilColoredQuadGuiElementRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;

public class RenderUtils {
    public static double ease(double start, double end, float speed) {
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }

    public static void drawHorizontalLine(DrawContext context, float i, float j, float k, int l) {
        if (j < i) {
            float m = i;
            i = j;
            j = m;
        }

        fillEvil(context, i, k, j + 1, k + 1, l);
    }

    public static void drawVerticalLine(DrawContext context, float i, float j, float k, int l) {
        if (k < j) {
            float m = j;
            j = k;
            k = m;
        }

        fillEvil(context, i, j + 1, i + 1, k, l);
    }

    public static void fillEvil(DrawContext context, float x, float y, float x2, float y2, int col) {
        context.state
                .addSimpleElement(
                        new EvilColoredQuadGuiElementRenderState(
                                RenderPipelines.GUI, TextureSetup.empty(), new Matrix3x2f(context.getMatrices()), x, y, x2, y2, col, col, context.scissorStack.peekLast()
                        )
                );
    }
}
