package com.goobercorp.gooberlib.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record FuckItGuiElementState(
		RenderPipeline pipeline,
		TextureSetup textureSetup,
		Matrix3x2fc matrix,
		Vec2f point1,
		Vec2f point2,
		Vec2f point3,
		Vec2f point4,
		int col1,
		int col2,
		int col3,
		int col4,
		@Nullable ScreenRect comp_4069,
		@Nullable ScreenRect comp_4274
) implements SimpleGuiElementRenderState {
	public FuckItGuiElementState(
			RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2fc matrix3x2fc, Vec2f i, Vec2f j, Vec2f k, Vec2f l, int m, int n, int o, int p, @Nullable ScreenRect screenRect
	) {
		this(renderPipeline, textureSetup, matrix3x2fc, i, j, k, l, m, n, o, p, screenRect, createBounds(0, 0, 1000, 1000, matrix3x2fc, screenRect));
	}

	@Override
	public void setupVertices(VertexConsumer vertexConsumer) {
		vertexConsumer.vertex(this.matrix(), point1.x, point1.y).color(this.col1());
		vertexConsumer.vertex(this.matrix(), point2.x, point2.y).color(this.col2());
		vertexConsumer.vertex(this.matrix(), point3.x, point3.y).color(this.col3());
		vertexConsumer.vertex(this.matrix(), point4.x, point4.y).color(this.col4());
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
