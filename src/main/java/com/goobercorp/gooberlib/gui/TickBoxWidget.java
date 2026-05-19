package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.builder.v3.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class TickBoxWidget extends ClickableWidget {
    BooleanOption opt;
    Tweener t;

    public TickBoxWidget(int i, int j, int k, int l, Text text) {
        super(i, j, k, l, text);
    }

    public TickBoxWidget(int x, int y, int width, int height, BooleanOption opt) {
        super(x, y, width, height, opt.name());
        this.opt = opt;
        t = new Tweener(() -> opt.value ? 1 : 0);
    }

    @Override
    protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
        t.update();
        RenderUtils.fillEvil(drawContext, getX(), getY(), getRight(), getBottom(), 0x80000000);
        float width = (getBottom() - 2) - getY() + 2;
        RenderUtils.drawBoxOutline(drawContext, getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2, -1);
        if (opt.value) {
            RenderUtils.fillEvil(drawContext, getRight() - width + 3, getY() + 3, getRight() - 3, getBottom() - 3, -1);
        }
        drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.message, getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, -1, true);
    }

    @Override
    public boolean mouseClicked(Click click, boolean bl) {
        if (RenderUtils.isInBounds(click.comp_4798(), click.comp_4799(), new ScreenRect(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
            opt.setValue(!opt.value);
        }
        return super.mouseClicked(click, bl);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }
}
