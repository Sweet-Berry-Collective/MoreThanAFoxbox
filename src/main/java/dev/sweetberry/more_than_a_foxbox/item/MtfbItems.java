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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public final class MtfbItems {
	private static final RegistryContext<Item> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.ITEM,
		MoreThanAFoxbox.ID
	);

	public static final Supplier<Item> PLUSHIE = CONTEXT.defer(
		"plushie",
		withProperties(properties -> new PlushieItem(properties.equippableUnswappable(EquipmentSlot.HEAD)))
	);

	public static final Supplier<Item> SPEAKER = CONTEXT.defer(
		"speaker",
		withProperties(Item::new)
	);

	public static final Supplier<Item> SQUEAKER = CONTEXT.defer(
		"squeaker",
		withProperties(Item::new)
	);

	public static final Supplier<Item> POLYFILL = CONTEXT.defer(
		"polyfill",
		withProperties(Item::new)
	);

	public static final Supplier<BlockItem> POLYFILL_BLOCK = CONTEXT.defer(
		"polyfill_block",
		withProperties(properties -> new BlockItem(MtfbBlocks.POLYFILL_BLOCK.get(), properties))
	);

	public static final Supplier<Item> CARDBOARD = CONTEXT.defer(
		"cardboard",
		withProperties(Item::new)
	);
	
	public static final Supplier<Item> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		withProperties(properties -> new BlockItem(MtfbBlocks.CARDBOARD_BOX.get(), properties.equippableUnswappable(EquipmentSlot.HEAD)))
	);

	private MtfbItems() {}
	
	public static void register() {
		CONTEXT.register();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Item> Function<ResourceKey<T>, T> withProperties(Function<Item.Properties, T> callback) {
		return key -> callback.apply(new Item.Properties().setId((ResourceKey<Item>) key));
	}
}
