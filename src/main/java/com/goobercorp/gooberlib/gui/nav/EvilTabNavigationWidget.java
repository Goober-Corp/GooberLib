package com.goobercorp.gooberlib.gui.nav;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.ScrollTweener;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilTabNavigationWidget extends AbstractContainerEventHandler implements Renderable, NarratableEntry {
	private static final Component USAGE_NARRATION_TEXT = Component.translatable("narration.tab_navigation.usage");
	private final LinearLayout grid = LinearLayout.horizontal();
	private int tabNavWidth;
	private final TabManager tabManager;
	private final ImmutableList<Tab> tabs;
	private final ArrayList<EvilTabButtonWidget> tabButtons = new ArrayList<>();
	private double targetX = 0d;
	private ScrollTweener scrollTweener;
	private int ticksSinceLastScroll = 0;
	private Vec2 prevDelta;

	EvilTabNavigationWidget(int i, TabManager tabManager, Iterable<Tab> iterable) {
		this.tabNavWidth = i;
		this.tabManager = tabManager;
		this.tabs = ImmutableList.copyOf(iterable);
		this.grid.defaultCellSetting().alignHorizontallyCenter();

		for (Tab tab : iterable) {
			tabButtons.add(this.grid.addChild(new EvilTabButtonWidget(tabManager, tab, 0, 24)));
		}

	}

	public void init() {
		int i = tabNavWidth - 10;
		int j = Mth.roundToward(i / this.tabs.size(), 2);

		for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
			tabButtonWidget.setWidth(Math.max(j, 100));
		}

		this.grid.arrangeElements();
		this.grid.setX(5);
		this.targetX = 5d;
		this.grid.setY(0);
		scrollTweener = new ScrollTweener(() -> targetX, number -> targetX = number, -grid.getWidth() + tabNavWidth / 2F, tabNavWidth / 2F, 20);
	}

	public static Builder builder(TabManager tabManager, int i) {
		return new Builder(tabManager, i);
	}

	public void setWidth(int i) {
		this.tabNavWidth = i;
	}

	@Override
	public boolean isMouseOver(double d, double e) {
		return d >= this.grid.getX() && e >= this.grid.getY() && d < this.grid.getX() + this.grid.getWidth() && e < this.grid.getY() + this.grid.getHeight();
	}

	public void tick() {
		ticksSinceLastScroll++;
		if (ticksSinceLastScroll > 2) {
			scrollTweener.setInteractionState(false);
		}
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent click, double deltaX, double deltaY) {
		prevDelta = new Vec2((float) (prevDelta.x + deltaX), (float) (prevDelta.y + deltaY));
		if (isDragging()) {
			this.targetX += (float) deltaX;
		}
		return super.mouseDragged(click, deltaX, deltaY);
	}

	@Override
	public void setFocused(boolean bl) {
		super.setFocused(bl);
		if (this.getFocused() != null) {
			this.setFocused(null);
		}
	}

	@Override
	public void setFocused(@Nullable GuiEventListener element) {
		super.setFocused(element);
		if (element instanceof EvilTabButtonWidget tabButtonWidget && tabButtonWidget.isActive()) {
			this.tabManager.setCurrentTab(tabButtonWidget.getTab(), true);
		}
	}


	@Nullable
	@Override
	public ComponentPath nextFocusPath(FocusNavigationEvent guiNavigation) {
		if (!this.isFocused()) {
			EvilTabButtonWidget tabButtonWidget = this.getCurrentTabButton();
			if (tabButtonWidget != null) {
				return ComponentPath.path(this, ComponentPath.leaf(tabButtonWidget));
			}
		}

		return guiNavigation instanceof FocusNavigationEvent.TabNavigation ? null : super.nextFocusPath(guiNavigation);
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return this.tabButtons;
	}

	public List<Tab> getTabs() {
		return this.tabs;
	}

	@Override
	public NarrationPriority narrationPriority() {
		return this.tabButtons.stream().map(AbstractWidget::narrationPriority).max(Comparator.naturalOrder()).orElse(NarrationPriority.NONE);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		prevDelta = new Vec2((float) click.x(), (float) click.y());
		Optional<GuiEventListener> optional = this.getChildAt(click.x(), click.y());
		if (optional.isEmpty()) {
			return false;
		} else {
			scrollTweener.setInteractionState(true);
			GuiEventListener element = optional.get();
			if (element.mouseClicked(click, bl) && element.shouldTakeFocusAfterInteraction()) {
				//TODO: this logic doesn't work for some reason after first opening screen
				if (!element.isFocused()) {
					this.setFocused(element);
					int currentTabIndex = getCurrentTabIndex();
					//the world isn't ready for this one yet
//					if (currentTabIndex == 0) {
//						this.targetX = 5d;
//					} else if (currentTabIndex == tabs.size() - 1) {
//						this.targetX = (double) tabNavWidth - 100 * tabs.size() - 5;
//					} else {
					this.targetX = -(getCurrentTabButton().getWidth() * currentTabIndex + 1) + tabNavWidth / 2.0 - getCurrentTabButton().getWidth() / 2F;
					if (!MainConfig.CATEGORY_ANIMATIONS.value) {
						scrollTweener.snap();
					}
//					}
				}
				if (click.button() == 0) {
					this.setDragging(true);
				}
			}

			return true;
		}
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
//		float speedX = (float) Math.abs(click.x() - prevDelta.x);
//		if (speedX > 1) {
//			this.targetX += speedX * 25;
//		}
//		if (click.button() == 0) {
//			scrollTweener.setInteractionState(false);
//		}
		return super.mouseReleased(click);
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationMessageBuilder) {
		Optional<EvilTabButtonWidget> optional = this.tabButtons.stream().filter(AbstractWidget::isHovered).findFirst().or(() -> Optional.ofNullable(this.getCurrentTabButton()));
		optional.ifPresent(tabButtonWidget -> {
			this.appendNarrations(narrationMessageBuilder.nest(), tabButtonWidget);
			tabButtonWidget.updateNarration(narrationMessageBuilder);
		});
		if (this.isFocused()) {
			narrationMessageBuilder.add(NarratedElementType.USAGE, USAGE_NARRATION_TEXT);
		}
	}

	protected void appendNarrations(NarrationElementOutput narrationMessageBuilder, EvilTabButtonWidget tabButtonWidget) {
		if (this.tabs.size() > 1) {
			int i = this.tabButtons.indexOf(tabButtonWidget);
			if (i != -1) {
				narrationMessageBuilder.add(NarratedElementType.POSITION, Component.translatable("narrator.position.tab", i + 1, this.tabs.size()));
			}
		}
	}


	@Override
	public void render(GuiGraphics drawContext, int i, int j, float f) {
//		if (MainConfig.ENABLE_INFINITE_TAB_SCROLLING.getValue()) {
//			scrollTweener.min = -10000;
//			scrollTweener.max = 10000;
//			page = scrollTweener.getI() / (tabNavWidth + 20);
//			if ((grid.getX() + grid.getWidth()) < tabNavWidth + 20) {
//				tabs.forEach(tab -> {
//					EvilTabButtonWidget widget = new EvilTabButtonWidget(tabManager, tab, 0, 24);
//					int meow = Mth.roundToward(tabs.indexOf(tab) / this.tabs.size(), 2);
//					widget.setWidth(Math.max(meow, 100));
//					tabButtons.add(grid.addChild(widget));
//				});
//				grid.arrangeElements();
//			}
//			if (tabButtons.size() > tabs.size() * 2 && scrollTweener.get() < tabNavWidth) {
//				tabs.forEach(_ -> tabButtons.removeFirst());
//			}
//		}

		scrollTweener.update();
		this.grid.setX(scrollTweener.getI());
		newMatrixScope(drawContext, stack -> {
			stack.translate(scrollTweener.getFloatingRemainder(), 0);
			RenderUtils.drawHorizontalLine(drawContext, -1, tabButtons.get(getCurrentTabIndex()).getX() - 1, this.grid.getY() + 1, 0x33FFFFFF);
			RenderUtils.drawHorizontalLine(drawContext, -1, tabButtons.get(getCurrentTabIndex()).getX() - 1, this.grid.getY(), 0xBF000000);

			RenderUtils.drawHorizontalLine(drawContext, tabButtons.get(getCurrentTabIndex()).getRight(), tabNavWidth, this.grid.getY() + 1, 0x33FFFFFF);
			RenderUtils.drawHorizontalLine(drawContext, tabButtons.get(getCurrentTabIndex()).getRight(), tabNavWidth, this.grid.getY(), 0xBF000000);

			for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
				tabButtonWidget.render(drawContext, i, j, f);
			}
		});
	}

	public void renderForBackgroundLayer(GuiGraphics drawContext) {

		drawContext.pose().pushMatrix();
		drawContext.pose().translate(scrollTweener.getFloatingRemainder(), 0);
		drawContext.hLine(0, tabButtons.getFirst().getX() - 1, this.grid.getY(), 0xBF000000);
		drawContext.hLine(tabButtons.getLast().getRight(), tabNavWidth, this.grid.getY(), 0xBF000000);

		for (EvilTabButtonWidget tabButtonWidget : this.tabButtons) {
			tabButtonWidget.renderForBackground(drawContext);
		}
		drawContext.pose().popMatrix();
	}

	@Override
	public ScreenRectangle getRectangle() {
		return this.grid.getRectangle();
	}

	public void selectTab(int i, boolean bl) {
		if (this.isFocused()) {
			this.setFocused(this.tabButtons.get(i));
		} else if (this.tabButtons.get(i).isActive()) {
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
	public boolean keyPressed(KeyEvent keyInput) {
		if (keyInput.hasControlDownWithQuirk()) {
			int i = this.getTabForKey(keyInput);
			if (i != -1) {
				this.selectTab(Mth.clamp(i, 0, this.tabs.size() - 1), true);
				return true;
			}
		}

		return false;
	}


	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		ticksSinceLastScroll = 0;
		scrollTweener.setInteractionState(true);
		if (targetX > scrollTweener.max || targetX < scrollTweener.min) {
			targetX += (float) (g * 10 * Math.min(1 / Math.abs(targetX - Math.clamp(targetX, scrollTweener.min, scrollTweener.max)), 1));
		} else {
			targetX += (float) (g * 10);
		}
		return false;
	}

	private int getTabForKey(KeyEvent keyInput) {
		return this.getTabForKey(this.getCurrentTabIndex(), keyInput);
	}

	private int getTabForKey(int i, KeyEvent keyInput) {
		int j = keyInput.getDigit();
		if (j != -1) {
			return Math.floorMod(j - 1, 10);
		} else if (keyInput.isCycleFocus() && i != -1) {
			int k = keyInput.hasShiftDown() ? i - 1 : i + 1;
			int l = Math.floorMod(k, this.tabs.size());
			return this.tabButtons.get(l).active ? l : this.getTabForKey(l, keyInput);
		} else {
			return -1;
		}
	}

	public int getCurrentTabIndex() {
		Tab tab = this.tabManager.getCurrentTab();
		return this.tabs.indexOf(tab);
	}

	private EvilTabButtonWidget getCurrentTabButton() {
		int i = this.getCurrentTabIndex();
		if (i == -1) {
			throw new IllegalStateException("i == -1");
		}
		return this.tabButtons.get(i);
	}


	public static class Builder {
		private final int width;
		private final TabManager tabManager;
		private final List<Tab> tabs = new ArrayList<>();

		Builder(TabManager tabManager, int i) {
			this.tabManager = tabManager;
			this.width = i;
		}

		public Builder tabs(Tab... tabs) {
			Collections.addAll(this.tabs, tabs);
			return this;
		}

		public EvilTabNavigationWidget build() {
			return new EvilTabNavigationWidget(this.width, this.tabManager, this.tabs);
		}
	}
}
