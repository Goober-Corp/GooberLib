package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

public record EvilerColoredQuadGuiElementRenderState(
		RenderPipeline pipeline,
		TextureSetup textureSetup,
		Matrix3x2fc matrix,
		float x1,
		float y1,
		float x2,
		float y2,
		float x3,
		float y3,
		float x4,
		float y4,
		int col1,
		int col2,
		int col3,
		int col4,
		@Nullable ScreenRectangle scissorArea,
		@Nullable ScreenRectangle bounds
) implements GuiElementRenderState {
	public EvilerColoredQuadGuiElementRenderState(
			RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2fc matrix3x2fc, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int col1, int col2, int col3, int col4, @Nullable ScreenRectangle screenRect
	) {
		this(renderPipeline, textureSetup, matrix3x2fc, x1, y1, x2, y2, x3, y3, x4, y4, col1, col2, col3, col4, screenRect, createBounds(x1, y1, x2, y2, x3, y3, x4, y4, matrix3x2fc, screenRect));
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer) {
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x3(), y3()).setColor(col3);
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x4(), y4()).setColor(col4);
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x1(), y1()).setColor(col1);
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x2(), y2()).setColor(col2);
	}

	@Nullable
	private static ScreenRectangle createBounds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Matrix3x2fc matrix3x2fc, @Nullable ScreenRectangle screenRect) {
		float minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		float minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
		float maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		float maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

		float width = maxX - minX;
		float height = maxY - minY;
		ScreenRectangle screenRect2 = new ScreenRectangle((int) minX, (int) minY, (int) width, (int) height).transformMaxBounds(matrix3x2fc);
		return screenRect != null ? screenRect.intersection(screenRect2) : screenRect2;
	}
}
