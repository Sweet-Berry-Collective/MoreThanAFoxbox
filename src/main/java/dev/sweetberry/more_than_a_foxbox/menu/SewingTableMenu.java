package dev.sweetberry.more_than_a_foxbox.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SewingTableMenu extends AbstractContainerMenu {
	// SLot 0: Outer shell material
	// Slot 1: Filling material
	// Slot 2: Upgrade material
	private final InputSlots inputSlots = new InputSlots(3);
	// Slot 3: Output slot
	private final Container resultSlots = new ResultContainer();

	protected SewingTableMenu(@Nullable MenuType<?> menuType, int containerId) {
		super(menuType, containerId);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return false;
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
