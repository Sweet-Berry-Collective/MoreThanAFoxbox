package dev.sweetberry.more_than_a_foxbox.registry;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MtfbRegistries {
	public static final ResourceKey<Registry<PlushieVariant>> PLUSHIE_VARIANT = create("plushie_variant");

	private static <T> ResourceKey<Registry<T>> create(String path) {
		return ResourceKey.createRegistryKey(MoreThanAFoxbox.id(path));
	}
}
