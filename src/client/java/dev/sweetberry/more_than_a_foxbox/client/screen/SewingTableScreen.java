/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.screen;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.menu.SewingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class SewingTableScreen extends AbstractContainerScreen<SewingTableMenu> {
	private static final ResourceLocation BG_LOCATION = MoreThanAFoxbox.id("textures/gui/container/sewing_table.png");
	private static final SlotBackground SHELL_BACKGROUND = new SlotBackground(
		0,
		MoreThanAFoxbox.id("container/slot/shell")
	);
	private static final SlotBackground FILLER_BACKGROUND = new SlotBackground(
		1,
		MoreThanAFoxbox.id("container/slot/filler")
	);
	private static final SlotBackground ESSENCE_BACKGROUND = new SlotBackground(
		2,
		MoreThanAFoxbox.id("container/slot/essence")
	);
	private static final SlotBackground UPGRADE_BACKGROUND = new SlotBackground(
		3,
		MoreThanAFoxbox.id("container/slot/upgrade")
	);

	public SewingTableScreen(SewingTableMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	public @NotNull SewingTableMenu getMenu() {
		return menu;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BG_LOCATION, leftPos, topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		SHELL_BACKGROUND.render(this, guiGraphics, leftPos, topPos);
		FILLER_BACKGROUND.render(this, guiGraphics, leftPos, topPos);
		ESSENCE_BACKGROUND.render(this, guiGraphics, leftPos, topPos);
		UPGRADE_BACKGROUND.render(this, guiGraphics, leftPos, topPos);
	}

	private record SlotBackground(int slot, ResourceLocation sprite) {
		public void render(SewingTableScreen screen, GuiGraphics guiGraphics, int x, int y) {
			var item = screen.menu.getSlot(slot);

			if (!item.hasItem())
				screen.renderIcon(item, sprite, guiGraphics, x, y);
		}
	}

	private void renderIcon(Slot slot, ResourceLocation icon, GuiGraphics guiGraphics, int x, int y) {
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x + slot.x, y + slot.y, 16, 16, ARGB.white(1));
	}
}
