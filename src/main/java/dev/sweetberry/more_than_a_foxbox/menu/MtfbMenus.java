/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.menu;

import java.util.function.Function;
import java.util.function.Supplier;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class MtfbMenus {
	private static final RegistryContext<MenuType<?>> CONTEXT = new RegistryContext<>(BuiltInRegistries.MENU, MoreThanAFoxbox.ID);

	public static final Supplier<MenuType<SewingTableMenu>> SEWING_TABLE = CONTEXT.defer(
		"sewing_table",
		withConstructor(SewingTableMenu::new)
	);

	public static void register() {
		CONTEXT.register();
	}

	private MtfbMenus() {}

	private static <T extends AbstractContainerMenu> Function<ResourceKey<MenuType<T>>, MenuType<T>> withConstructor(MenuType.MenuSupplier<T> supplier) {
		return key -> new MenuType<>(supplier, FeatureFlagSet.of());
	}
}
