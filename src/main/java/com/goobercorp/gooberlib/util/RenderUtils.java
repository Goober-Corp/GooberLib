package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.joml.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
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

	@SuppressWarnings("unused")
	public static void drawHorizontalLine(GuiGraphics context, float x1, float x2, float y, int col, int col2) {
		if (x2 < x1) {
			float m = x1;
			x1 = x2;
			x2 = m;
		}

		fillEvil(context, x1, y, x2 + 1, y + 1, col, col2);
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
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

	@SuppressWarnings("unused")
	public static void drawTriangle(GuiGraphics context, float ax, float ay, float bx, float by, float cx, float cy, int col1, int col2, int col3) {
		drawQuad(context, cx, cy, bx, by, ax, ay, ax, ay, col3, col2, col1, col1);
	}

	@SuppressWarnings("unused")
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

	public static ACM newMatrixScope(GuiGraphics context) {
		context.pose().pushMatrix();
		return new ACM(context.pose());
	}

	@SuppressWarnings("ExternalizableWithoutPublicNoArgConstructor")
	public static class ACM extends Matrix3x2fStack implements AutoCloseable {
		private final Matrix3x2fStack wrapped;
		@SuppressWarnings("unused")
		private float m00, m01, m10, m11, m20, m21; // prevent them from being accessed directly

		@Override
		public void close() {
			wrapped.popMatrix();
		}

		public Matrix3x2fStack stack() {
			return wrapped;
		}

		public ACM(Matrix3x2fStack wrapped) {
			super();
			this.wrapped = wrapped;
		}

		@Override
		public Matrix3x2fStack clear() {
			return wrapped.clear();
		}

		@Override
		public Matrix3x2fStack pushMatrix() {
			return wrapped.pushMatrix();
		}

		@Override
		public Matrix3x2fStack popMatrix() {
			return wrapped.popMatrix();
		}

		@Override
		public int hashCode() {
			return wrapped.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return wrapped.equals(obj);
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			wrapped.writeExternal(out);
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException {
			wrapped.readExternal(in);
		}

		@SuppressWarnings("MethodDoesntCallSuperMethod")
		@Override
		public Object clone() throws CloneNotSupportedException {
			return wrapped.clone();
		}

		@Override
		public float m00() {
			return wrapped.m00();
		}

		@Override
		public float m01() {
			return wrapped.m01();
		}

		@Override
		public float m10() {
			return wrapped.m10();
		}

		@Override
		public float m11() {
			return wrapped.m11();
		}

		@Override
		public float m20() {
			return wrapped.m20();
		}

		@Override
		public float m21() {
			return wrapped.m21();
		}

		@Override
		public Matrix3x2f set(Matrix3x2fc m) {
			return wrapped.set(m);
		}

		@Override
		public Matrix3x2f set(Matrix2fc m) {
			return wrapped.set(m);
		}

		@Override
		public Matrix3x2f mul(Matrix3x2fc right) {
			return wrapped.mul(right);
		}

		@Override
		public Matrix3x2f mul(Matrix3x2fc right, Matrix3x2f dest) {
			return wrapped.mul(right, dest);
		}

		@Override
		public Matrix3x2f mulLocal(Matrix3x2fc left) {
			return wrapped.mulLocal(left);
		}

		@Override
		public Matrix3x2f mulLocal(Matrix3x2fc left, Matrix3x2f dest) {
			return wrapped.mulLocal(left, dest);
		}

		@Override
		public Matrix3x2f set(float m00, float m01, float m10, float m11, float m20, float m21) {
			return wrapped.set(m00, m01, m10, m11, m20, m21);
		}

		@Override
		public Matrix3x2f set(float[] m) {
			return wrapped.set(m);
		}

		@Override
		public Matrix3x2f set(float[] m, int off) {
			return wrapped.set(m, off);
		}

		@Override
		public float determinant() {
			return wrapped.determinant();
		}

		@Override
		public Matrix3x2f invert() {
			return wrapped.invert();
		}

		@Override
		public Matrix3x2f invert(Matrix3x2f dest) {
			return wrapped.invert(dest);
		}

		@Override
		public Matrix3x2f translation(float x, float y) {
			return wrapped.translation(x, y);
		}

		@Override
		public Matrix3x2f translation(Vector2fc offset) {
			return wrapped.translation(offset);
		}

		@Override
		public Matrix3x2f setTranslation(float x, float y) {
			return wrapped.setTranslation(x, y);
		}

		@Override
		public Matrix3x2f setTranslation(Vector2f offset) {
			return wrapped.setTranslation(offset);
		}

		@Override
		public Matrix3x2f translate(float x, float y, Matrix3x2f dest) {
			return wrapped.translate(x, y, dest);
		}

		@Override
		public Matrix3x2f translate(float x, float y) {
			return wrapped.translate(x, y);
		}

		@Override
		public Matrix3x2f translate(Vector2fc offset, Matrix3x2f dest) {
			return wrapped.translate(offset, dest);
		}

		@Override
		public Matrix3x2f translate(Vector2fc offset) {
			return wrapped.translate(offset);
		}

		@Override
		public Matrix3x2f translateLocal(Vector2fc offset) {
			return wrapped.translateLocal(offset);
		}

		@Override
		public Matrix3x2f translateLocal(Vector2fc offset, Matrix3x2f dest) {
			return wrapped.translateLocal(offset, dest);
		}

		@Override
		public Matrix3x2f translateLocal(float x, float y, Matrix3x2f dest) {
			return wrapped.translateLocal(x, y, dest);
		}

		@Override
		public Matrix3x2f translateLocal(float x, float y) {
			return wrapped.translateLocal(x, y);
		}

		@Override
		public String toString() {
			return wrapped.toString();
		}

		@Override
		public String toString(NumberFormat formatter) {
			return wrapped.toString(formatter);
		}

		@Override
		public Matrix3x2f get(Matrix3x2f dest) {
			return wrapped.get(dest);
		}

		@Override
		public FloatBuffer get(FloatBuffer buffer) {
			return wrapped.get(buffer);
		}

		@Override
		public FloatBuffer get(int index, FloatBuffer buffer) {
			return wrapped.get(index, buffer);
		}

		@Override
		public ByteBuffer get(ByteBuffer buffer) {
			return wrapped.get(buffer);
		}

		@Override
		public ByteBuffer get(int index, ByteBuffer buffer) {
			return wrapped.get(index, buffer);
		}

		@Override
		public FloatBuffer get3x3(FloatBuffer buffer) {
			return wrapped.get3x3(buffer);
		}

		@Override
		public FloatBuffer get3x3(int index, FloatBuffer buffer) {
			return wrapped.get3x3(index, buffer);
		}

		@Override
		public ByteBuffer get3x3(ByteBuffer buffer) {
			return wrapped.get3x3(buffer);
		}

		@Override
		public ByteBuffer get3x3(int index, ByteBuffer buffer) {
			return wrapped.get3x3(index, buffer);
		}

		@Override
		public FloatBuffer get4x4(FloatBuffer buffer) {
			return wrapped.get4x4(buffer);
		}

		@Override
		public FloatBuffer get4x4(int index, FloatBuffer buffer) {
			return wrapped.get4x4(index, buffer);
		}

		@Override
		public ByteBuffer get4x4(ByteBuffer buffer) {
			return wrapped.get4x4(buffer);
		}

		@Override
		public ByteBuffer get4x4(int index, ByteBuffer buffer) {
			return wrapped.get4x4(index, buffer);
		}

		@Override
		public Matrix3x2fc getToAddress(long address) {
			return wrapped.getToAddress(address);
		}

		@Override
		public Matrix3x2fc getTransposedToAddress(long address) {
			return wrapped.getTransposedToAddress(address);
		}

		@Override
		public float[] get(float[] arr, int offset) {
			return wrapped.get(arr, offset);
		}

		@Override
		public float[] get(float[] arr) {
			return wrapped.get(arr);
		}

		@Override
		public float[] get3x3(float[] arr, int offset) {
			return wrapped.get3x3(arr, offset);
		}

		@Override
		public float[] get3x3(float[] arr) {
			return wrapped.get3x3(arr);
		}

		@Override
		public float[] get4x4(float[] arr, int offset) {
			return wrapped.get4x4(arr, offset);
		}

		@Override
		public float[] get4x4(float[] arr) {
			return wrapped.get4x4(arr);
		}

		@Override
		public Matrix3x2f set(FloatBuffer buffer) {
			return wrapped.set(buffer);
		}

		@Override
		public Matrix3x2f set(ByteBuffer buffer) {
			return wrapped.set(buffer);
		}

		@Override
		public Matrix3x2f set(int index, FloatBuffer buffer) {
			return wrapped.set(index, buffer);
		}

		@Override
		public Matrix3x2f set(int index, ByteBuffer buffer) {
			return wrapped.set(index, buffer);
		}

		@Override
		public Matrix3x2f setFromAddress(long address) {
			return wrapped.setFromAddress(address);
		}

		@Override
		public Matrix3x2f setTransposedFromAddress(long address) {
			return wrapped.setTransposedFromAddress(address);
		}

		@Override
		public Matrix3x2f zero() {
			return wrapped.zero();
		}

		@Override
		public Matrix3x2f identity() {
			return wrapped.identity();
		}

		@Override
		public Matrix3x2f scale(float x, float y, Matrix3x2f dest) {
			return wrapped.scale(x, y, dest);
		}

		@Override
		public Matrix3x2f scale(float x, float y) {
			return wrapped.scale(x, y);
		}

		@Override
		public Matrix3x2f scale(Vector2fc xy) {
			return wrapped.scale(xy);
		}

		@Override
		public Matrix3x2f scale(Vector2fc xy, Matrix3x2f dest) {
			return wrapped.scale(xy, dest);
		}

		@Override
		public Matrix3x2f scale(float xy, Matrix3x2f dest) {
			return wrapped.scale(xy, dest);
		}

		@Override
		public Matrix3x2f scale(float xy) {
			return wrapped.scale(xy);
		}

		@Override
		public Matrix3x2f scaleLocal(float x, float y, Matrix3x2f dest) {
			return wrapped.scaleLocal(x, y, dest);
		}

		@Override
		public Matrix3x2f scaleLocal(float x, float y) {
			return wrapped.scaleLocal(x, y);
		}

		@Override
		public Matrix3x2f scaleLocal(float xy, Matrix3x2f dest) {
			return wrapped.scaleLocal(xy, dest);
		}

		@Override
		public Matrix3x2f scaleLocal(float xy) {
			return wrapped.scaleLocal(xy);
		}

		@Override
		public Matrix3x2f scaleAround(float sx, float sy, float ox, float oy, Matrix3x2f dest) {
			return wrapped.scaleAround(sx, sy, ox, oy, dest);
		}

		@Override
		public Matrix3x2f scaleAround(float sx, float sy, float ox, float oy) {
			return wrapped.scaleAround(sx, sy, ox, oy);
		}

		@Override
		public Matrix3x2f scaleAround(float factor, float ox, float oy, Matrix3x2f dest) {
			return wrapped.scaleAround(factor, ox, oy, dest);
		}

		@Override
		public Matrix3x2f scaleAround(float factor, float ox, float oy) {
			return wrapped.scaleAround(factor, ox, oy);
		}

		@Override
		public Matrix3x2f scaleAroundLocal(float sx, float sy, float ox, float oy, Matrix3x2f dest) {
			return wrapped.scaleAroundLocal(sx, sy, ox, oy, dest);
		}

		@Override
		public Matrix3x2f scaleAroundLocal(float factor, float ox, float oy, Matrix3x2f dest) {
			return wrapped.scaleAroundLocal(factor, ox, oy, dest);
		}

		@Override
		public Matrix3x2f scaleAroundLocal(float sx, float sy, float sz, float ox, float oy, float oz) {
			return wrapped.scaleAroundLocal(sx, sy, sz, ox, oy, oz);
		}

		@Override
		public Matrix3x2f scaleAroundLocal(float factor, float ox, float oy) {
			return wrapped.scaleAroundLocal(factor, ox, oy);
		}

		@Override
		public Matrix3x2f scaling(float factor) {
			return wrapped.scaling(factor);
		}

		@Override
		public Matrix3x2f scaling(float x, float y) {
			return wrapped.scaling(x, y);
		}

		@Override
		public Matrix3x2f rotation(float angle) {
			return wrapped.rotation(angle);
		}

		@Override
		public Vector3f transform(Vector3f v) {
			return wrapped.transform(v);
		}

		@Override
		public Vector3f transform(Vector3fc v, Vector3f dest) {
			return wrapped.transform(v, dest);
		}

		@Override
		public Vector3f transform(float x, float y, float z, Vector3f dest) {
			return wrapped.transform(x, y, z, dest);
		}

		@Override
		public Vector2f transformPosition(Vector2f v) {
			return wrapped.transformPosition(v);
		}

		@Override
		public Vector2f transformPosition(Vector2fc v, Vector2f dest) {
			return wrapped.transformPosition(v, dest);
		}

		@Override
		public Vector2f transformPosition(float x, float y, Vector2f dest) {
			return wrapped.transformPosition(x, y, dest);
		}

		@Override
		public Vector2f transformDirection(Vector2f v) {
			return wrapped.transformDirection(v);
		}

		@Override
		public Vector2f transformDirection(Vector2fc v, Vector2f dest) {
			return wrapped.transformDirection(v, dest);
		}

		@Override
		public Vector2f transformDirection(float x, float y, Vector2f dest) {
			return wrapped.transformDirection(x, y, dest);
		}

		@Override
		public Matrix3x2f rotate(float ang) {
			return wrapped.rotate(ang);
		}

		@Override
		public Matrix3x2f rotate(float ang, Matrix3x2f dest) {
			return wrapped.rotate(ang, dest);
		}

		@Override
		public Matrix3x2f rotateLocal(float ang, Matrix3x2f dest) {
			return wrapped.rotateLocal(ang, dest);
		}

		@Override
		public Matrix3x2f rotateLocal(float ang) {
			return wrapped.rotateLocal(ang);
		}

		@Override
		public Matrix3x2f rotateAbout(float ang, float x, float y) {
			return wrapped.rotateAbout(ang, x, y);
		}

		@Override
		public Matrix3x2f rotateAbout(float ang, float x, float y, Matrix3x2f dest) {
			return wrapped.rotateAbout(ang, x, y, dest);
		}

		@Override
		public Matrix3x2f rotateTo(Vector2fc fromDir, Vector2fc toDir, Matrix3x2f dest) {
			return wrapped.rotateTo(fromDir, toDir, dest);
		}

		@Override
		public Matrix3x2f rotateTo(Vector2fc fromDir, Vector2fc toDir) {
			return wrapped.rotateTo(fromDir, toDir);
		}

		@Override
		public Matrix3x2f view(float left, float right, float bottom, float top, Matrix3x2f dest) {
			return wrapped.view(left, right, bottom, top, dest);
		}

		@Override
		public Matrix3x2f view(float left, float right, float bottom, float top) {
			return wrapped.view(left, right, bottom, top);
		}

		@Override
		public Matrix3x2f setView(float left, float right, float bottom, float top) {
			return wrapped.setView(left, right, bottom, top);
		}

		@Override
		public Vector2f origin(Vector2f origin) {
			return wrapped.origin(origin);
		}

		@Override
		public float[] viewArea(float[] area) {
			return wrapped.viewArea(area);
		}

		@Override
		public Vector2f positiveX(Vector2f dir) {
			return wrapped.positiveX(dir);
		}

		@Override
		public Vector2f normalizedPositiveX(Vector2f dir) {
			return wrapped.normalizedPositiveX(dir);
		}

		@Override
		public Vector2f positiveY(Vector2f dir) {
			return wrapped.positiveY(dir);
		}

		@Override
		public Vector2f normalizedPositiveY(Vector2f dir) {
			return wrapped.normalizedPositiveY(dir);
		}

		@Override
		public Vector2f unproject(float winX, float winY, int[] viewport, Vector2f dest) {
			return wrapped.unproject(winX, winY, viewport, dest);
		}

		@Override
		public Vector2f unprojectInv(float winX, float winY, int[] viewport, Vector2f dest) {
			return wrapped.unprojectInv(winX, winY, viewport, dest);
		}

		@Override
		public Matrix3x2f shearX(float yFactor) {
			return wrapped.shearX(yFactor);
		}

		@Override
		public Matrix3x2f shearX(float yFactor, Matrix3x2f dest) {
			return wrapped.shearX(yFactor, dest);
		}

		@Override
		public Matrix3x2f shearY(float xFactor) {
			return wrapped.shearY(xFactor);
		}

		@Override
		public Matrix3x2f shearY(float xFactor, Matrix3x2f dest) {
			return wrapped.shearY(xFactor, dest);
		}

		@Override
		public Matrix3x2f span(Vector2f corner, Vector2f xDir, Vector2f yDir) {
			return wrapped.span(corner, xDir, yDir);
		}

		@Override
		public boolean testPoint(float x, float y) {
			return wrapped.testPoint(x, y);
		}

		@Override
		public boolean testCircle(float x, float y, float r) {
			return wrapped.testCircle(x, y, r);
		}

		@Override
		public boolean testAar(float minX, float minY, float maxX, float maxY) {
			return wrapped.testAar(minX, minY, maxX, maxY);
		}

		@Override
		public boolean equals(Matrix3x2fc m, float delta) {
			return wrapped.equals(m, delta);
		}

		@Override
		public boolean isFinite() {
			return wrapped.isFinite();
		}
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
