/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.data.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.item.MtfbItems;
import dev.sweetberry.more_than_a_foxbox.menu.SewingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SewingTableScreen extends AbstractContainerScreen<SewingTableMenu> {
	private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller");
	private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller_disabled");
	private static final Identifier RECIPE_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_selected");
	private static final Identifier RECIPE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_highlighted");
	private static final Identifier RECIPE_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe");
	private static final Identifier BG_LOCATION = MoreThanAFoxbox.id("textures/gui/container/sewing_table.png");
	private static final int SCROLLER_WIDTH = 12;
	private static final int SCROLLER_HEIGHT = 15;
	private static final int RECIPES_COLUMNS = 4;
	private static final int RECIPES_ROWS = 3;
	private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
	private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
	private static final int SCROLLER_FULL_HEIGHT = 54;
	private static final int RECIPES_X = 52;
	private static final int RECIPES_Y = 14;
	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;

	public SewingTableScreen(SewingTableMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		menu.registerUpdateListener(this::containerChanged);
		--titleLabelY;
	}

	@Override
	public @NotNull SewingTableMenu getMenu() {
		return menu;
	}

	@Override
	public void extractBackground(final @NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int xo = this.leftPos;
		int yo = this.topPos;
		graphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		int sy = (int)(41.0F * this.scrollOffs);
		Identifier sprite = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
		int scrollerXStart = xo + 119;
		int scrollerYStart = yo + 15;
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, scrollerXStart, scrollerYStart + sy, SCROLLER_WIDTH, SCROLLER_HEIGHT);
		if (mouseX >= scrollerXStart && mouseY >= scrollerYStart && mouseX < scrollerXStart + SCROLLER_WIDTH && mouseY < scrollerYStart + SCROLLER_FULL_HEIGHT) {
			if (this.isScrollBarActive()) {
				graphics.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
			} else {
				graphics.requestCursor(CursorTypes.NOT_ALLOWED);
			}
		}

		int x = this.leftPos + RECIPES_X;
		int y = this.topPos + RECIPES_Y;
		int endIndex = this.startIndex + 12;
		this.extractButtons(graphics, mouseX, mouseY, x, y, endIndex);
		this.extractRecipes(graphics, x, y, endIndex);
	}

	@Override
	protected void extractTooltip(final @NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
		super.extractTooltip(graphics, mouseX, mouseY);
		if (this.displayRecipes) {
			int edgeLeft = this.leftPos + RECIPES_X;
			int edgeTop = this.topPos + RECIPES_Y;
			int endIndex = this.startIndex + 12;
			List<Holder.Reference<PlushieVariant>> visibleRecipes = menu.getCraftablePlushieVariants();

			for(int index = this.startIndex; index < endIndex && index < visibleRecipes.size(); ++index) {
				int posIndex = index - this.startIndex;
				int itemLeft = edgeLeft + posIndex % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
				int itemRight = edgeTop + posIndex / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
				if (mouseX >= itemLeft && mouseX < itemLeft + RECIPES_IMAGE_SIZE_WIDTH && mouseY >= itemRight && mouseY < itemRight + RECIPES_IMAGE_SIZE_HEIGHT) {
					ItemStack stack = new ItemStack(MtfbItems.PLUSHIE.get());
					stack.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(visibleRecipes.get(index).key()));
					graphics.setTooltipForNextFrame(this.font, stack, mouseX, mouseY);
				}
			}
		}
	}

	private void extractButtons(final GuiGraphicsExtractor graphics, final int xm, final int ym, final int x, final int y, final int endIndex) {
		for(int index = this.startIndex; index < endIndex && index < this.menu.getNumberOfVisibleRecipes(); ++index) {
			int posIndex = index - this.startIndex;
			int posX = x + posIndex % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
			int row = posIndex / RECIPES_COLUMNS;
			int posY = y + row * RECIPES_IMAGE_SIZE_HEIGHT + 2;
			Identifier sprite;
			if (index == this.menu.getSelectedPlushieIndex()) {
				sprite = RECIPE_SELECTED_SPRITE;
			} else if (xm >= posX && ym >= posY && xm < posX + RECIPES_IMAGE_SIZE_WIDTH && ym < posY + RECIPES_IMAGE_SIZE_HEIGHT) {
				sprite = RECIPE_HIGHLIGHTED_SPRITE;
			} else {
				sprite = RECIPE_SPRITE;
			}

			int textureY = posY - 1;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, posX, textureY, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
			if (xm >= posX && ym >= textureY && xm < posX + RECIPES_IMAGE_SIZE_WIDTH && ym < textureY + RECIPES_IMAGE_SIZE_HEIGHT) {
				graphics.requestCursor(CursorTypes.POINTING_HAND);
			}
		}
	}

	private void extractRecipes(final GuiGraphicsExtractor graphics, final int x, final int y, final int endIndex) {
		List<Holder.Reference<PlushieVariant>> visibleRecipes = menu.getCraftablePlushieVariants();

		for(int index = this.startIndex; index < endIndex && index < visibleRecipes.size(); ++index) {
			int posIndex = index - this.startIndex;
			int posX = x + posIndex % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
			int row = posIndex / RECIPES_COLUMNS;
			int posY = y + row * RECIPES_IMAGE_SIZE_HEIGHT + 2;
			ItemStack stack = new ItemStack(MtfbItems.PLUSHIE.get());
			stack.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(visibleRecipes.get(index).key()));
			graphics.item(stack, posX, posY);
		}
	}

	public boolean mouseClicked(final @NonNull MouseButtonEvent event, final boolean doubleClick) {
		if (this.displayRecipes) {
			int xo = this.leftPos + RECIPES_X;
			int yo = this.topPos + RECIPES_Y;
			int endIndex = this.startIndex + 12;

			for(int index = this.startIndex; index < endIndex; ++index) {
				int posIndex = index - this.startIndex;
				double xx = event.x() - (double)(xo + posIndex % 4 * 16);
				double yy = event.y() - (double)(yo + posIndex / 4 * 18);
				if (xx >= (double)0.0F && yy >= (double)0.0F && xx < (double)16.0F && yy < (double)18.0F && this.menu.clickMenuButton(this.minecraft.player, index)) {
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
					return true;
				}
			}

			xo = this.leftPos + 119;
			yo = this.topPos + 9;
			if (event.x() >= (double)xo && event.x() < (double)(xo + 12) && event.y() >= (double)yo && event.y() < (double)(yo + SCROLLER_FULL_HEIGHT)) {
				this.scrolling = true;
			}
		}

		return super.mouseClicked(event, doubleClick);
	}

	public boolean mouseDragged(final @NonNull MouseButtonEvent event, final double dx, final double dy) {
		if (this.scrolling && this.isScrollBarActive()) {
			int yscr = this.topPos + 14;
			int yscr2 = yscr + SCROLLER_FULL_HEIGHT;
			this.scrollOffs = ((float)event.y() - (float)yscr - (float)(SCROLLER_HEIGHT / 2)) / ((float)(yscr2 - yscr) - (float)SCROLLER_FULL_HEIGHT);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + (double)0.5F) * 4;
			return true;
		} else {
			return super.mouseDragged(event, dx, dy);
		}
	}

	public boolean mouseReleased(final @NonNull MouseButtonEvent event) {
		this.scrolling = false;
		return super.mouseReleased(event);
	}

	public boolean mouseScrolled(final double x, final double y, final double scrollX, final double scrollY) {
		if (!super.mouseScrolled(x, y, scrollX, scrollY)) {
			if (this.isScrollBarActive()) {
				int offscreenRows = this.getOffscreenRows();
				float scrolledDelta = (float) scrollY / (float) offscreenRows;
				this.scrollOffs = Mth.clamp(this.scrollOffs - scrolledDelta, 0.0F, 1.0F);
				this.startIndex = (int) ((double) (this.scrollOffs * (float) offscreenRows) + (double) 0.5F) * 4;
			}

		}
		return true;
	}

	private boolean isScrollBarActive() {
		return this.displayRecipes && menu.getNumberOfVisibleRecipes() > 12;
	}

	protected int getOffscreenRows() {
		return (menu.getNumberOfVisibleRecipes() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - RECIPES_ROWS;
	}

	private void containerChanged() {
		this.displayRecipes = menu.canCreatePlushie();
		this.scrollOffs = 0.0F;
		this.startIndex = 0;
	}
}
