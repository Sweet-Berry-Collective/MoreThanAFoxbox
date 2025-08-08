package dev.sweetberry.more_than_a_foxbox.item;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class MtfbItems {
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

	public static void register() {
		CONTEXT.register();
	}
}
