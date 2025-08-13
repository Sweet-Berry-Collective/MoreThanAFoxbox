/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.screen;

import dev.sweetberry.more_than_a_foxbox.menu.SewingTableMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SewingTableScreen extends Screen implements MenuAccess<SewingTableMenu> {
	public final SewingTableMenu menu;

	public SewingTableScreen(SewingTableMenu menu, Inventory playerInventory, Component title) {
		super(title);

		this.menu = menu;
	}

	@Override
	public @NotNull SewingTableMenu getMenu() {
		return menu;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
