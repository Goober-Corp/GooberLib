package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

public class RenderUtils {
	public static double ease(double start, double end, float speed) {
		var dt = 1.0F / Minecraft.getInstance().getFps();
		return start + (end - start) * (1 - exp(-dt * speed));
	}

	public static final RenderPipeline.Snippet GUI_SNIPPET = RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
			.withVertexShader("core/gui")
			.withFragmentShader("core/gui")
			.withBlend(BlendFunction.TRANSLUCENT)
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.buildSnippet();

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

	//TODO: this sucks
	public static void drawThinningHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col, int col2, float thickness, boolean flip) {
		if (x2 < x1) {
			float m = x1;
			x1 = x2;
			x2 = m;
		}

		fillEviler(context, x1, y - thickness, x2 + 1, y + thickness, col, col2, flip);
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

	public static void fillEviler(GuiGraphics context, float x, float y, float x2, float y2, int col, int col2, boolean flip) {
		context.guiRenderState
				.submitGuiElement(
						new EvilerColoredQuadGuiElementRenderState(
								RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), x, y, x2, y2, col, col, col2, col2, context.scissorStack.peek(), flip
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
