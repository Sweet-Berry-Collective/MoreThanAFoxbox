/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.item;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class MtfbItems {
	private static final RegistryContext<Item, Item.Properties> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.ITEM,
		MoreThanAFoxbox.ID,
		key -> new Item.Properties().setId(key)
	);

	public static final RegistryContext.Value<Item> SPEAKER = CONTEXT.defer(
		"speaker",
		Item::new
	);

	public static final RegistryContext.Value<Item> SQUEAKER = CONTEXT.defer(
		"squeaker",
		Item::new
	);

	// TODO: Polyfill block that acts like cobwebs
	public static final RegistryContext.Value<Item> POLYFILL = CONTEXT.defer(
		"polyfill",
		Item::new
	);

	public static final RegistryContext.Value<Item> CARDBOARD = CONTEXT.defer(
		"cardboard",
		Item::new
	);
	
	public static final RegistryContext.Value<Item> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		properties -> new BlockItem(MtfbBlocks.CARDBOARD_BOX.get(), properties)
	);

	private MtfbItems() {}
	
	public static void register() {
		CONTEXT.register();
	}
}
