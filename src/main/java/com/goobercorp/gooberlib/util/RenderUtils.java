package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;

import static java.lang.Math.exp;

public class RenderUtils {
	public static double ease(double start, double end, float speed) {
		var dt = 1.0F / Minecraft.getInstance().getFps();
		return start + (end - start) * (1 - exp(-dt * speed));
	}

	public static void drawHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col) {
		if (x2 < x1) {
			float m = x1;
			x1 = x2;
			x2 = m;
		}

		fillEvil(context, x1, y, x2 + 1, y + 1, col);
	}

	public static void drawHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col, int col2) {
		if (x2 < x1) {
			float m = x1;
			x1 = x2;
			x2 = m;
		}

		fillEvil(context, x1, y, x2 + 1, y + 1, col, col2);
	}

	public static void drawThinningHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col1, int col2, float startingThickness) {
		if (x1 < x2) {
			var ax = x1;
			var ay = y;
			var bx = x2;
			var by = y + startingThickness / 2;
			var cx = x2;
			var cy = y - startingThickness / 2;
			drawQuad(context, ax, ay - 0.25f, ax, ay + 0.25f, bx, by, cx, cy, col1, col1, col2, col2);
		} else {
			var temp = x1;
			x1 = x2;
			x2 = temp;

			var ax = x1;
			var ay = y - startingThickness / 2;
			var bx = x1;
			var by = y + startingThickness / 2;
			var cx = x2;
			var cy = y;
			drawQuad(context, ax, ay, bx, by, cx, cy + 0.25f, cx, cy - 0.25f, col1, col1, col2, col2);
		}
	}

	public static void drawTriangle(GuiGraphics context, float ax, float ay, float bx, float by, float cx, float cy, int col1, int col2, int col3) {
		drawQuad(context, cx, cy, bx, by, ax, ay, ax, ay, col3, col2, col1, col1);
	}

	public static void drawHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col, int col2, int col3, int col4) {
		if (x2 < x1) {
			float m = x1;
			x1 = x2;
			x2 = m;
		}

		fillEvil(context, x1, y, x2 + 1, y + 1, col, col2, col3, col4);
	}

	public static void drawVerticalLine(GuiGraphics context, float x, float y1, float y2, int col) {
		if (y2 < y1) {
			float m = y1;
			y1 = y2;
			y2 = m;
		}

		fillEvil(context, x, y1 + 1, x + 1, y2, col);
	}

	public static void fillEvil(GuiGraphics context, float x, float y, float x2, float y2, int col) {
		context.guiRenderState
				.submitGuiElement(
						new EvilColoredQuadGuiElementRenderState(
								RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x, y, x2, y2, col, col, col, col, context.scissorStack.peek()
						)
				);
	}

	public static void fillEvil(GuiGraphics context, float x, float y, float x2, float y2, int col, RenderPipeline pipeline) {
		context.guiRenderState
				.submitGuiElement(
						new EvilColoredQuadGuiElementRenderState(
								pipeline, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x, y, x2, y2, col, col, col, col, context.scissorStack.peek()
						)
				);
	}

	public static void fillEvil(GuiGraphics context, float x, float y, float x2, float y2, int col, int col2) {
		context.guiRenderState
				.submitGuiElement(
						new EvilColoredQuadGuiElementRenderState(
								RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x, y, x2, y2, col, col, col2, col2, context.scissorStack.peek()
						)
				);
	}

	public static void drawQuad(GuiGraphics context, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int col1, int col2, int col3, int col4) {
		context.guiRenderState
				.submitGuiElement(
						new EvilerColoredQuadGuiElementRenderState(
								RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x1, y1, x2, y2, x3, y3, x4, y4, col1, col2, col3, col4, context.scissorStack.peek()
						)
				);
	}

	public static void fillEvil(GuiGraphics context, float x, float y, float x2, float y2, int col, int col2, int col3, int col4) {
		context.guiRenderState
				.submitGuiElement(
						new EvilColoredQuadGuiElementRenderState(
								RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x, y, x2, y2, col, col2, col3, col4, context.scissorStack.peek()
						)
				);
	}

	public static void drawBoxOutline(GuiGraphics context, float x, float y, float x2, float y2, int col) {
		drawHorizontalLine(context, x, x2, y, col);
		drawHorizontalLine(context, x, x2, y2, col);

		drawVerticalLine(context, x, y, y2, col);
		drawVerticalLine(context, x2, y, y2, col);
	}

	public static void newMatrixScope(GuiGraphics context, Consumer<Matrix3x2fStack> function) {
		context.pose().pushMatrix();
		function.accept(context.pose());
		context.pose().popMatrix();
	}

	public static boolean isInBounds(double mouseX, double mouseY, ScreenRectangle rect) {
		return mouseX >= rect.left() && mouseY >= rect.top() && mouseX < rect.right() && mouseY < rect.bottom();
	}

	public static void breakpoint(String reason) {
		Instant instant = Instant.now();
		GooberLibEntrypoint.LOGGER.warn("Did you remember to set a breakpoint here?");
		boolean bl = Duration.between(instant, Instant.now()).toMillis() > 500L;
		if (!bl) {
			GooberLibEntrypoint.LOGGER.warn("Breakpoint because {}", reason);
		}
	}
}
