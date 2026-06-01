package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.function.Function;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;

public class EvilBaseWidget extends ClickableWidget {

	protected Tweener hoverTweener;
	protected boolean mouseDown = false;
	protected float verticalPosOffset = 0;
	protected float horizontalPosOffset = 0;
	protected Tweener clickTweener;
	//TODO: add back reference to BaseOption so we can easily reset on mouse middle click here
	private final Text name;
	private final Function<BaseOption<?>, Text> valueFormatter;

	public EvilBaseWidget(Text name, int x, int y, int width, int height, Function<BaseOption<?>, Text> valueFormatter) {
		super(x, y, width, height, name);
		this.name = name;
		this.valueFormatter = valueFormatter;
		hoverTweener = new Tweener(() -> this.hovered || mouseDown ? 1 : 0, 10);
		clickTweener = new Tweener(() -> this.mouseDown ? 1 : 0);
	}

	public EvilBaseWidget(Text name, int x, int y, int width, int height) {
		this(name, x, y, width, height, BaseOption::name);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		boolean yeah = super.mouseClicked(click, bl);
		if (yeah) {
			mouseDown = true;
		}
		return yeah;
	}

	@Override
	public boolean mouseReleased(Click click) {
		boolean yeah = super.mouseReleased(click);
		if (yeah) {
			mouseDown = false;
		}
		return yeah;
	}

	@Override
	protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
		if (!mouseDown) {
			verticalPosOffset = (float) ease(verticalPosOffset, 0, 15);
			horizontalPosOffset = (float) ease(horizontalPosOffset, 0, 15);
		}
		hoverTweener.update();
		clickTweener.update();
//		stack.translate(horizontalPosOffset, verticalPosOffset);
		drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("gooberlib", "widget/button"), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0x80808080);
		drawText(drawContext);
		RenderUtils.drawBoxOutline(drawContext, this.getX() + clickTweener.getF(), this.getY() + clickTweener.getF(), this.getRight() - 1 - clickTweener.getF(), this.getBottom() - 1 - clickTweener.getF(), ColorHelper.lerp(hoverTweener.getF(), 0xFF000000, MainConfig.primaryCol));
	}

	protected void drawText(DrawContext drawContext) {
		drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, name, this.getX() + this.width / 2, this.getY() + this.height / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol);
	}


	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

	}

	@Override
	public boolean mouseDragged(Click click, double d, double e) {
		//TODO: i have no idea if this works how i think it should. it's good enough
		this.verticalPosOffset += (float) e * 0.025F * Math.min(1 / Math.abs(verticalPosOffset) / 2, 1);
		this.horizontalPosOffset += (float) d * 0.025F * Math.min(1 / Math.abs(horizontalPosOffset) / 2, 1);
		return super.mouseDragged(click, d, e);
	}

	public Function<BaseOption<?>, Text> getValueFormatter() {
		return valueFormatter;
	}
}
