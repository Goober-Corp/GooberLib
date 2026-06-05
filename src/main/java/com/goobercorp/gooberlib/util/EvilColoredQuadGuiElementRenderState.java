package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

public record EvilColoredQuadGuiElementRenderState(
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
		@Nullable ScreenRectangle scissorArea,
		@Nullable ScreenRectangle bounds
) implements GuiElementRenderState {
	public EvilColoredQuadGuiElementRenderState(
			RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2fc matrix3x2fc, float i, float j, float k, float l, int m, int n, int o, int p, @Nullable ScreenRectangle screenRect
	) {
		this(renderPipeline, textureSetup, matrix3x2fc, i, j, k, l, m, n, o, p, screenRect, getBounds((int) i, (int) j, (int) k, (int) l, matrix3x2fc, screenRect));
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer) {
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x(), this.y()).setColor(this.col1());
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x(), this.y2()).setColor(this.col2());
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x2(), this.y2()).setColor(this.col3());
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x2(), this.y()).setColor(this.col4());
	}

	@Nullable
	private static ScreenRectangle getBounds(int i, int j, int k, int l, Matrix3x2fc matrix3x2fc, @Nullable ScreenRectangle screenRect) {
		ScreenRectangle screenRect2 = new ScreenRectangle(i, j, k - i, l - j).transformMaxBounds(matrix3x2fc);
		return screenRect != null ? screenRect.intersection(screenRect2) : screenRect2;
	}
}
