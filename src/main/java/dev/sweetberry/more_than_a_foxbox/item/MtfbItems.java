/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.item;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.data.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class MtfbItems {
	private static final RegistryContext<Item> ITEM_CONTEXT = new RegistryContext<>(
		BuiltInRegistries.ITEM,
		MoreThanAFoxbox.ID
	);
	private static final RegistryContext<CreativeModeTab> TAB_CONTEXT = new RegistryContext<>(
		BuiltInRegistries.CREATIVE_MODE_TAB,
		MoreThanAFoxbox.ID
	);

	public static final Supplier<Item> PLUSHIE = ITEM_CONTEXT.defer(
		"plushie",
		withProperties(properties -> new PlushieItem(properties
			.equippableUnswappable(EquipmentSlot.HEAD)
			.stacksTo(1)
			.component(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(PlushieVariant.PLACEHOLDER, Optional.empty()))
		))
	);

	public static final Supplier<Item> SPEAKER = ITEM_CONTEXT.defer(
		"speaker",
		withProperties(Item::new)
	);

	public static final Supplier<Item> SQUEAKER = ITEM_CONTEXT.defer(
		"squeaker",
		withProperties(Item::new)
	);

	public static final Supplier<Item> POLYFILL = ITEM_CONTEXT.defer(
		"polyfill",
		withProperties(Item::new)
	);

	public static final Supplier<BlockItem> POLYFILL_BLOCK = ITEM_CONTEXT.defer(
		"polyfill_block",
		withProperties(properties -> new BlockItem(MtfbBlocks.POLYFILL_BLOCK.get(), properties))
	);

	public static final Supplier<Item> CARDBOARD = ITEM_CONTEXT.defer(
		"cardboard",
		withProperties(Item::new)
	);
	
	public static final Supplier<Item> CARDBOARD_BOX = ITEM_CONTEXT.defer(
		"cardboard_box",
		withProperties(properties -> new BlockItem(MtfbBlocks.CARDBOARD_BOX.get(), properties.equippableUnswappable(EquipmentSlot.HEAD)))
	);

	public static final Supplier<BlockItem> SEWING_TABLE = ITEM_CONTEXT.defer(
		"sewing_table",
		withProperties(properties -> new BlockItem(MtfbBlocks.SEWING_TABLE.get(), properties))
	);

	public static final Supplier<CreativeModeTab> TAB = TAB_CONTEXT.defer(
		"tab",
		creativeModeTab(
			CARDBOARD_BOX,
			MtfbItems::populateCreativeModeTab
		)
	);

	private MtfbItems() {}
	
	public static void register() {
		ITEM_CONTEXT.register();
		TAB_CONTEXT.register();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Item> Function<ResourceKey<T>, T> withProperties(Function<Item.Properties, T> callback) {
		return key -> callback.apply(new Item.Properties().setId((ResourceKey<Item>) key));
	}

	private static Function<ResourceKey<CreativeModeTab>, CreativeModeTab> creativeModeTab(
		Supplier<? extends Item> displayItem,
		CreativeModeTab.DisplayItemsGenerator generator
	) {
		return key -> CreativeModeTab
			.builder(CreativeModeTab.Row.TOP, 0)
			.title(Component.translatable("item_group."+key.identifier().toLanguageKey()))
			.displayItems(generator)
			.icon(displayItem.get()::getDefaultInstance)
			.build();
	}

	public static void populateCreativeModeTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
		output.acceptAll(List.of(
			SPEAKER.get().getDefaultInstance(),
			SQUEAKER.get().getDefaultInstance(),
			POLYFILL.get().getDefaultInstance(),
			POLYFILL_BLOCK.get().getDefaultInstance(),
			CARDBOARD.get().getDefaultInstance(),
			CARDBOARD_BOX.get().getDefaultInstance(),
			SEWING_TABLE.get().getDefaultInstance()
		));


		var maybeRegistry = itemDisplayParameters.holders().lookup(MtfbRegistries.PLUSHIE_VARIANT);

		if (maybeRegistry.isEmpty())
			return;

		var registry = (Registry<PlushieVariant>) maybeRegistry.get();

		var stack = PLUSHIE.get().getDefaultInstance();

		for (var value :
			registry
				.entrySet()
				.stream()
				.sorted(
					Comparator
						.comparing(it ->
							it
								.getKey()
								.identifier()
								.toString()
						)
				)
				.toList()
		) {
			if (value.getKey() == PlushieVariant.PLACEHOLDER)
				continue;

			stack.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(value.getKey(), Optional.empty()));

			output.accept(stack.copy());

			stack.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(value.getKey(), Optional.of(PlushieDataComponent.SoundType.SPEAKER)));

			output.accept(stack.copy());

			stack.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(value.getKey(), Optional.of(PlushieDataComponent.SoundType.SQUEAKER)));

			output.accept(stack.copy());
		}
	}
}
