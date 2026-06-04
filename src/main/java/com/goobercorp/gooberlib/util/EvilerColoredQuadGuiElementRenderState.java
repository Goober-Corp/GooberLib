package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.NonNull;
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
		@Nullable ScreenRectangle scissorArea,
		@Nullable ScreenRectangle bounds,
		boolean flip
) implements GuiElementRenderState {
	public EvilerColoredQuadGuiElementRenderState(
			RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2fc matrix3x2fc, float i, float j, float k, float l, int m, int n, int o, int p, @Nullable ScreenRectangle screenRect, boolean flip
	) {
		this(renderPipeline, textureSetup, matrix3x2fc, i, j, k, l, m, n, o, p, screenRect, createBounds((int) i, (int) j, (int) k, (int) l, matrix3x2fc, screenRect), flip);
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer) {
		float val = (y() + y2()) / 2F;
		//top left
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x(), flip ? y() : val).setColor(col1);
		//bottom left
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x(), flip ? y2() : val).setColor(this.col2());
		//bottom right
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x2(), flip ? val : y2()).setColor(col3);
		//top right
		vertexConsumer.addVertexWith2DPose(this.matrix(), this.x2(), flip ? val : y()).setColor(this.col4());
	}

	@Override
	public @NonNull RenderPipeline pipeline() {
		return pipeline;
	}

	@Override
	public @NonNull TextureSetup textureSetup() {
		return textureSetup;
	}
	

	@Nullable
	private static ScreenRectangle createBounds(int i, int j, int k, int l, Matrix3x2fc matrix3x2fc, @Nullable ScreenRectangle screenRect) {
		ScreenRectangle screenRect2 = new ScreenRectangle(i, j, k - i, l - j).transformMaxBounds(matrix3x2fc);
		return screenRect != null ? screenRect.intersection(screenRect2) : screenRect2;
	}

}
