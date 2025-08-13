/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.menu;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SewingTableMenu extends AbstractContainerMenu {
	private final InputSlots inputSlots = new InputSlots(3);
	private final Container resultSlots = new ResultContainer();

	public SewingTableMenu(int containerId, Inventory playerInventory) {
		super(MtfbMenus.SEWING_TABLE.get(), containerId);

		addSlot(new Slot(inputSlots, 0, 20, -3));
		addSlot(new Slot(inputSlots, 1, 20, 33));
		addSlot(new Slot(inputSlots, 2, 20, 69));

		addSlot(new Slot(resultSlots, 3, 143, 33));

		addStandardInventorySlots(playerInventory, 8, 84);

		MoreThanAFoxbox.LOGGER.info("h");
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	private class InputSlots extends SimpleContainer {
		public InputSlots(int count) {
			super(count);
		}

		@Override
		public void setChanged() {
			super.setChanged();
			SewingTableMenu.this.slotsChanged(this);
		}
	}
}
