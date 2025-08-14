/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.menu;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.recipe.ContainerRecipeInput;
import dev.sweetberry.more_than_a_foxbox.recipe.MtfbRecipes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SewingTableMenu extends AbstractContainerMenu {
	public static final int RESULT_SLOT = 4;
	public static final int INV_SLOT_START = RESULT_SLOT + 1;
	public static final int INV_SLOT_END = INV_SLOT_START + 27;
	public static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;

	private final InputContainer inputContainer = new InputContainer(4);
	private final ResultContainer resultContainer = new ResultContainer();

	private final ContainerRecipeInput recipeInput = new ContainerRecipeInput(inputContainer);

	private final ContainerLevelAccess access;
	private final Player player;

	public SewingTableMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public SewingTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(MtfbMenus.SEWING_TABLE.get(), containerId);

		this.access = access;
		player = playerInventory.player;

		addSlot(new Slot(inputContainer, 0, 30, 35-18));
		addSlot(new Slot(inputContainer, 1, 30, 35));
		addSlot(new Slot(inputContainer, 2, 30, 35+18));
		addSlot(new Slot(inputContainer, 3, 66, 35));

		addSlot(new ResultSlot(resultContainer, 3, 124, 34));

		addStandardInventorySlots(playerInventory, 8, 84);
	}

	private void setResult(ItemStack result, ServerPlayer player) {
		resultContainer.setItem(0, result);
		setRemoteSlot(RESULT_SLOT, result);
		player.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), RESULT_SLOT, result));
	}

	private void setupRecipeSlot(
		ServerLevel level,
		ServerPlayer player
	) {
		var optional = level.getServer().getRecipeManager().getRecipeFor(MtfbRecipes.SEWING_TYPE.get(), recipeInput, level);

		if (optional.isEmpty()) {
			setResult(ItemStack.EMPTY, player);

			return;
		}

		var recipe = optional.get();

		resultContainer.setRecipeUsed(recipe);

		var result = recipe.value().assemble(recipeInput, level.registryAccess());

		if (!result.isItemEnabled(level.enabledFeatures())) {
			setResult(ItemStack.EMPTY, player);

			return;
		}

		setResult(result, player);
	}

	@Override
	public void slotsChanged(Container container) {
		access.execute((level, blockPos) -> {
			if (level instanceof ServerLevel serverLevel)
				setupRecipeSlot(serverLevel, (ServerPlayer) player);
		});
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
		return stillValid(access, player, MtfbBlocks.SEWING_TABLE.get());
	}

	@Override
	public void removed(Player player) {
		access.execute((level, blockPos) -> clearContainer(player, inputContainer));
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
				var slot = getSlot(i);

				var item = slot.getItem();

				var count = item.getCount();

				if (count == 1)
					refreshRecipes = true;

				if (count == 1 && item.getRecipeRemainder() != ItemStack.EMPTY)
					slot.set(item.getRecipeRemainder());
				else
					slot.remove(1);
			}

			if (refreshRecipes)
				slotsChanged(inputContainer);

			super.onTake(player, stack);
		}

		private List<ItemStack> getRelevantItems() {
			return inputContainer.items;
		}
	}
}
