package com.goobercorp.gooberlib.gui;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EvilTabNavigationWidget extends AbstractParentElement implements Drawable, Selectable {
    private static final Text USAGE_NARRATION_TEXT = Text.translatable("narration.tab_navigation.usage");
    private final DirectionalLayoutWidget grid = DirectionalLayoutWidget.horizontal();
    private int tabNavWidth;
    private final TabManager tabManager;
    private final ImmutableList<Tab> tabs;
    private final ImmutableList<EvilTabButtonWidget> tabButtons;

    EvilTabNavigationWidget(int i, TabManager tabManager, Iterable<Tab> iterable) {
        this.tabNavWidth = i;
        this.tabManager = tabManager;
        this.tabs = ImmutableList.copyOf(iterable);
        this.grid.getMainPositioner().alignHorizontalCenter();
        ImmutableList.Builder<EvilTabButtonWidget> builder = ImmutableList.builder();

        for (Tab tab : iterable) {
            builder.add(this.grid.add(new EvilTabButtonWidget(tabManager, tab, 0, 24)));
        }

        this.tabButtons = builder.build();
    }

    public static EvilTabNavigationWidget.Builder builder(TabManager tabManager, int i) {
        return new EvilTabNavigationWidget.Builder(tabManager, i);
    }

    public void setWidth(int i) {
        this.tabNavWidth = i;
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return d >= this.grid.getX() && e >= this.grid.getY() && d < this.grid.getX() + this.grid.getWidth() && e < this.grid.getY() + this.grid.getHeight();
    }

    @Override
    public void setFocused(boolean bl) {
        super.setFocused(bl);
        if (this.getFocused() != null) {
            this.setFocused(null);
        }
    }

    @Override
    public void setFocused(@Nullable Element element) {
        super.setFocused(element);
        if (element instanceof EvilTabButtonWidget tabButtonWidget && tabButtonWidget.isInteractable()) {
            this.tabManager.setCurrentTab(tabButtonWidget.getTab(), true);
        }
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation guiNavigation) {
        if (!this.isFocused()) {
            EvilTabButtonWidget tabButtonWidget = this.getCurrentTabButton();
            if (tabButtonWidget != null) {
                return GuiNavigationPath.of(this, GuiNavigationPath.of(tabButtonWidget));
            }
        }

        return guiNavigation instanceof GuiNavigation.Tab ? null : super.getNavigationPath(guiNavigation);
    }

    @Override
    public List<? extends Element> children() {
        return this.tabButtons;
    }

    public List<Tab> getTabs() {
        return this.tabs;
    }

    @Override
    public Selectable.SelectionType getType() {
        return this.tabButtons.stream().map(ClickableWidget::getType).max(Comparator.naturalOrder()).orElse(SelectionType.NONE);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder narrationMessageBuilder) {
        Optional<EvilTabButtonWidget> optional = this.tabButtons
                .stream()
                .filter(ClickableWidget::isHovered)
                .findFirst()
                .or(() -> Optional.ofNullable(this.getCurrentTabButton()));
        optional.ifPresent(tabButtonWidget -> {
            this.appendNarrations(narrationMessageBuilder.nextMessage(), tabButtonWidget);
            tabButtonWidget.appendNarrations(narrationMessageBuilder);
        });
        if (this.isFocused()) {
            narrationMessageBuilder.put(NarrationPart.USAGE, USAGE_NARRATION_TEXT);
        }
    }

    protected void appendNarrations(NarrationMessageBuilder narrationMessageBuilder, EvilTabButtonWidget tabButtonWidget) {
        if (this.tabs.size() > 1) {
            int i = this.tabButtons.indexOf(tabButtonWidget);
            if (i != -1) {
                narrationMessageBuilder.put(NarrationPart.POSITION, Text.translatable("narrator.position.tab", i + 1, this.tabs.size()));
            }
        }
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        drawContext.drawHorizontalLine(0, tabButtons.get(getCurrentTabIndex()).getX() - 1, this.grid.getY() + 1, 0x33FFFFFF);
        drawContext.drawHorizontalLine(0, tabButtons.get(getCurrentTabIndex()).getX() - 1, this.grid.getY(), 0xBF000000);

        drawContext.drawHorizontalLine(tabButtons.get(getCurrentTabIndex()).getRight(), tabNavWidth, this.grid.getY() + 1, 0x33FFFFFF);
        drawContext.drawHorizontalLine(tabButtons.get(getCurrentTabIndex()).getRight(), tabNavWidth, this.grid.getY(), 0xBF000000);

        for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
            tabButtonWidget.render(drawContext, i, j, f);
        }
    }

    public void renderForBackgroundLayer(DrawContext drawContext) {
        drawContext.drawHorizontalLine(0, tabButtons.getFirst().getX() - 1, this.grid.getY(), 0xBF000000);
        drawContext.drawHorizontalLine(tabButtons.getLast().getRight(), tabNavWidth, this.grid.getY(), 0xBF000000);

        for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
            tabButtonWidget.renderForBackground(drawContext);
        }
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return this.grid.getNavigationFocus();
    }

    public void init() {
        int i = Math.min(400, this.tabNavWidth) - 28;
        int j = MathHelper.roundUpToMultiple(i / this.tabs.size(), 2);

        for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
            tabButtonWidget.setWidth(j);
        }

        this.grid.refreshPositions();
        this.grid.setX(MathHelper.roundUpToMultiple((this.tabNavWidth - i) / 2, 2));
        this.grid.setY(0);
    }

    public void selectTab(int i, boolean bl) {
        if (this.isFocused()) {
            this.setFocused(this.tabButtons.get(i));
        } else if (this.tabButtons.get(i).isInteractable()) {
            this.tabManager.setCurrentTab(this.tabs.get(i), bl);
        }
    }

    public void setTabActive(int i, boolean bl) {
        if (i >= 0 && i < this.tabButtons.size()) {
            this.tabButtons.get(i).active = bl;
        }
    }

    public void setTabTooltip(int i, @Nullable Tooltip tooltip) {
        if (i >= 0 && i < this.tabButtons.size()) {
            this.tabButtons.get(i).setTooltip(tooltip);
        }
    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        if (keyInput.hasCtrlOrCmd()) {
            int i = this.getTabForKey(keyInput);
            if (i != -1) {
                this.selectTab(MathHelper.clamp(i, 0, this.tabs.size() - 1), true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return super.mouseScrolled(d, e, f, g);
    }

    private int getTabForKey(KeyInput keyInput) {
        return this.getTabForKey(this.getCurrentTabIndex(), keyInput);
    }

    private int getTabForKey(int i, KeyInput keyInput) {
        int j = keyInput.asNumber();
        if (j != -1) {
            return Math.floorMod(j - 1, 10);
        } else if (keyInput.isTab() && i != -1) {
            int k = keyInput.hasShift() ? i - 1 : i + 1;
            int l = Math.floorMod(k, this.tabs.size());
            return this.tabButtons.get(l).active ? l : this.getTabForKey(l, keyInput);
        } else {
            return -1;
        }
    }

    private int getCurrentTabIndex() {
        Tab tab = this.tabManager.getCurrentTab();
        return this.tabs.indexOf(tab);
    }

    @Nullable
    private EvilTabButtonWidget getCurrentTabButton() {
        int i = this.getCurrentTabIndex();
        return i != -1 ? this.tabButtons.get(i) : null;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final int width;
        private final TabManager tabManager;
        private final List<Tab> tabs = new ArrayList<>();

        Builder(TabManager tabManager, int i) {
            this.tabManager = tabManager;
            this.width = i;
        }

        public EvilTabNavigationWidget.Builder tabs(Tab... tabs) {
            Collections.addAll(this.tabs, tabs);
            return this;
        }

        public EvilTabNavigationWidget build() {
            return new EvilTabNavigationWidget(this.width, this.tabManager, this.tabs);
        }
    }
}
