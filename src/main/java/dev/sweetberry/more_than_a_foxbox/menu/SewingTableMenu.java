/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.menu;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SewingTableMenu extends AbstractContainerMenu {
	public static final int RESULT_SLOT = 3;
	public static final int INV_SLOT_START = RESULT_SLOT + 1;
	public static final int INV_SLOT_END = INV_SLOT_START + 27;
	public static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;

	private final InputContainer inputContainer = new InputContainer(3);
	private final ResultContainer resultContainer = new ResultContainer();

	public SewingTableMenu(int containerId, Inventory playerInventory) {
		super(MtfbMenus.SEWING_TABLE.get(), containerId);

		addSlot(new Slot(inputContainer, 0, 20, 34-18));
		addSlot(new Slot(inputContainer, 1, 20, 34));
		addSlot(new Slot(inputContainer, 2, 20, 34+18));

		addSlot(new ResultSlot(resultContainer, 3, 143, 34));

		addStandardInventorySlots(playerInventory, 8, 84);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		var slot = slots.get(index);

		if (!slot.hasItem())
			return ItemStack.EMPTY;

		var slotStack = slot.getItem();
		var item = slotStack.getItem();
		var resultStack = slotStack.copy();

		if (index == RESULT_SLOT) {
			item.onCraftedBy(slotStack, player);

			if (!moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, true))
				return ItemStack.EMPTY;

			slot.onQuickCraft(slotStack, resultStack);
		} else if (index < RESULT_SLOT) {
			if (!moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false))
				return ItemStack.EMPTY;
		} else if (moveItemStackTo(slotStack, 0, RESULT_SLOT, false)) {
			return ItemStack.EMPTY;
		} else if (index < INV_SLOT_END) {
			if (!moveItemStackTo(slotStack, INV_SLOT_END, USE_ROW_SLOT_END, false))
				return ItemStack.EMPTY;
		} else if (index < USE_ROW_SLOT_END && !moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
			return ItemStack.EMPTY;
		}

		if (slotStack.isEmpty())
			slot.setByPlayer(ItemStack.EMPTY);

		slot.setChanged();
		if (slotStack.getCount() == resultStack.getCount())
			return ItemStack.EMPTY;

		slot.onTake(player, slotStack);
		if (index == RESULT_SLOT)
			player.drop(slotStack, false);

		broadcastChanges();

		return resultStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	private class InputContainer extends SimpleContainer {
		public InputContainer(int count) {
			super(count);
		}

		@Override
		public void setChanged() {
			super.setChanged();
			SewingTableMenu.this.slotsChanged(this);
		}
	}

	private class ResultSlot extends Slot {
		public ResultSlot(Container container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}

		@Override
		public void onTake(Player player, ItemStack stack) {
			stack.onCraftedBy(player, stack.getCount());
			resultContainer.awardUsedRecipes(player, getRelevantItems());

			var refreshRecipes = false;

			for (int i = 0; i < RESULT_SLOT; i++) {
				var item = getSlot(i).remove(1);

				if (!item.isEmpty())
					refreshRecipes = true;
			}

			if (refreshRecipes) {
//				setupResultSlot(selectedRecipeIndex.get());
			}

//			access.execute((level, blockPos) -> {
//				long l = level.getGameTime();
//				if (lastSoundTime != l) {
//					level.playSound(null, blockPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
//					lastSoundTime = l;
//				}
//			});
			super.onTake(player, stack);
		}

		private List<ItemStack> getRelevantItems() {
			return inputContainer.items;
		}
	}
}
