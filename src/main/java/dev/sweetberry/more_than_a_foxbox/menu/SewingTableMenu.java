/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.menu;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.data.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.item.MtfbItems;
import dev.sweetberry.more_than_a_foxbox.network.clientbound.ClientboundSewingTablePlushies;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SewingTableMenu extends AbstractContainerMenu {
	private static final Identifier EMPTY_SLOT_FILLER = MoreThanAFoxbox.id("container/slot/filler");
	private static final Identifier EMPTY_SLOT_UPGRADE = MoreThanAFoxbox.id("container/slot/upgrade");
	private static final Identifier EMPTY_SLOT_SHELL = MoreThanAFoxbox.id("container/slot/shell");

	public static final int RESULT_SLOT = 3;
	public static final int INV_SLOT_START = RESULT_SLOT + 1;
	public static final int INV_SLOT_END = INV_SLOT_START + 27;
	public static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;

	private final InputContainer inputContainer = new InputContainer(3);
	private final ResultContainer resultContainer = new ResultContainer();

	private final Player player;
	private final ContainerLevelAccess access;
	private Runnable slotUpdateListener = () -> {};

	private List<Holder.Reference<PlushieVariant>> craftablePlushieVariants = Collections.emptyList();
	private final DataSlot selectedPlushieIndex = DataSlot.standalone();


	public SewingTableMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public SewingTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(MtfbMenus.SEWING_TABLE.get(), containerId);

		this.player = playerInventory.player;
		this.access = access;
		addDataSlot(selectedPlushieIndex);

		addSlot(new Slot(inputContainer, 0, 11, 25) {
			@Override
			public boolean mayPlace(@NonNull ItemStack stack) {
				return stack.is(MtfbItems.POLYFILL_TAG);
			}

			public @NonNull Identifier getNoItemIcon() {
				return EMPTY_SLOT_FILLER;
			}
		});
		addSlot(new Slot(inputContainer, 1, 11, 43) {
			@Override
			public boolean mayPlace(@NonNull ItemStack stack) {
				return stack.is(ItemTags.WOOL);
			}

			public @NonNull Identifier getNoItemIcon() {
				return EMPTY_SLOT_SHELL;
			}
		});
		addSlot(new Slot(inputContainer, 2, 29, 34) {
			@Override
			public boolean mayPlace(@NonNull ItemStack stack) {
				return stack.is(MtfbItems.PLUSHIE_UPGRADES);
			}

			public @NonNull Identifier getNoItemIcon() {
				return EMPTY_SLOT_UPGRADE;
			}
		});

		addSlot(new ResultSlot(resultContainer, 3, 143, 49));

		addStandardInventorySlots(playerInventory, 8, 84);
	}

	public boolean clickMenuButton(@NonNull Player player, int id) {
		if (this.selectedPlushieIndex.get() == id) {
			return false;
		} else {
			if (this.isValidRecipeIndex(id)) {
				this.selectedPlushieIndex.set(id);
				this.setupResultSlot(id);
			}

			return true;
		}
	}

	@Override
	public void slotsChanged(@NonNull Container container) {
		access.execute((level, _) -> {
			if (
				player instanceof ServerPlayer serverPlayer
				&& (
					craftablePlushieVariants.isEmpty() && canCreatePlushie()
					|| !craftablePlushieVariants.isEmpty() && !canCreatePlushie()
				)
			) {
				setupPlushieList(level.registryAccess());
				ServerPlayNetworking.send(serverPlayer, ClientboundSewingTablePlushies.INSTANCE);
			}
		});
	}

	public void setupPlushieList(RegistryAccess registryAccess) {
		this.selectedPlushieIndex.set(-1);
		this.resultContainer.setItem(0, ItemStack.EMPTY);
		craftablePlushieVariants = Collections.unmodifiableList(
			PlushieVariant.orderedPlushies(registryAccess, holder ->
				!holder.is(PlushieVariant.PLACEHOLDER) && !holder.is(PlushieVariant.EXCLUDED_IN_SEWING_TABLE))
		);
	}

	private void setupResultSlot(int id) {
		Optional<ResourceKey<PlushieVariant>> optional;
		if (!this.craftablePlushieVariants.isEmpty() && this.isValidRecipeIndex(id)) {
			optional = Optional.of(craftablePlushieVariants.get(id).key());
		} else {
			optional = Optional.empty();
		}

		optional.ifPresentOrElse((plushieKey) -> {
			ItemStack plushie = MtfbItems.PLUSHIE.get().getDefaultInstance();
			plushie.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(plushieKey, getSoundType()));
			this.resultContainer.setItem(0, plushie);
		}, () -> {
			this.resultContainer.setItem(0, ItemStack.EMPTY);
		});
		this.broadcastChanges();
	}

	public PlushieDataComponent.@Nullable SoundType getSoundType() {
		ItemStack upgradeStack = inputContainer.getItem(2);
		if (upgradeStack.is(MtfbItems.SPEAKER.get())) {
			return PlushieDataComponent.SoundType.SPEAKER;
		}
		if (upgradeStack.is(MtfbItems.SQUEAKER.get())) {
			return PlushieDataComponent.SoundType.SQUEAKER;
		}
		return null;
	}

	public boolean canCreatePlushie() {
		ItemStack polyfill = inputContainer.getItem(0);
		ItemStack wool = inputContainer.getItem(1);
		return !polyfill.isEmpty() && !wool.isEmpty();
	}

	public int getSelectedPlushieIndex() {
		return selectedPlushieIndex.get();
	}

	public List<Holder.Reference<PlushieVariant>> getCraftablePlushieVariants() {
		return craftablePlushieVariants;
	}

	public int getNumberOfVisibleRecipes() {
		return this.craftablePlushieVariants.size();
	}

	private boolean isValidRecipeIndex(int recipeIndex) {
		return recipeIndex >= 0 && recipeIndex < this.craftablePlushieVariants.size();
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NonNull Player player, int index) {
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
	public boolean stillValid(@NonNull Player player) {
		return stillValid(access, player, MtfbBlocks.SEWING_TABLE.get());
	}

	@Override
	public void removed(@NonNull Player player) {
		access.execute((_, _) -> clearContainer(player, inputContainer));
	}

	public void registerUpdateListener(final Runnable slotUpdateListener) {
		this.slotUpdateListener = slotUpdateListener;
	}

	private class InputContainer extends SimpleContainer {
		public InputContainer(int count) {
			super(count);
		}

		@Override
		public void setChanged() {
			super.setChanged();
			SewingTableMenu.this.slotsChanged(this);
			SewingTableMenu.this.slotUpdateListener.run();
		}
	}

	private class ResultSlot extends Slot {
		public ResultSlot(Container container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		@Override
		public boolean mayPlace(@NonNull ItemStack stack) {
			return false;
		}

		@Override
		public void onTake(@NonNull Player player, ItemStack stack) {
			stack.onCraftedBy(player, stack.getCount());
			resultContainer.awardUsedRecipes(player, getRelevantItems());

			for (int i = 0; i < RESULT_SLOT; i++) {
				var slot = getSlot(i);

				var item = slot.getItem();

				var count = item.getCount();

				if (item.getCraftingRemainder() != null && item.getCraftingRemainder().create() != ItemStack.EMPTY) {
					if (count == 1) {
						slot.set(item.getCraftingRemainder().create());
					} else {
						player.getInventory().placeItemBackInInventory(item.getCraftingRemainder().create());
					}
				} else
					slot.remove(1);
			}
			if (!stack.isEmpty()) {
				SewingTableMenu.this.setupResultSlot(selectedPlushieIndex.get());
			}

			super.onTake(player, stack);
		}

		private List<ItemStack> getRelevantItems() {
			return inputContainer.items;
		}
	}
}
