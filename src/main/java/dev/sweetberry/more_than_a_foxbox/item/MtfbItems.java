package dev.sweetberry.more_than_a_foxbox.item;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class MtfbItems {
	public static final ResourceKey<Item> SPEAKER = key("speaker");
	public static final ResourceKey<Item> SQUEAKER = key("squeaker");
	public static final ResourceKey<Item> CARDBOARD = key("cardboard");

	public static void init() {
		register(SPEAKER, Item::new);
		register(SQUEAKER, Item::new);
		register(CARDBOARD, Item::new);
	}

	public static ResourceKey<Item> key(String path) {
		return ResourceKey.create(Registries.ITEM, MoreThanAFoxbox.id(path));
	}

	private static <T extends Item> void register(ResourceKey<Item> key, Function<Item.Properties, T> func) {
		Registry.register(BuiltInRegistries.ITEM, key, func.apply(new Item.Properties()));
	}
}
