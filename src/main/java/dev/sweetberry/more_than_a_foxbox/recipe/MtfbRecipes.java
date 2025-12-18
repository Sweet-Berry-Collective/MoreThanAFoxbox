/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.recipe;

import java.util.function.Supplier;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class MtfbRecipes {
	private static final RegistryContext<RecipeType<?>> TYPE_CONTEXT = new RegistryContext<>(BuiltInRegistries.RECIPE_TYPE, MoreThanAFoxbox.ID);
	private static final RegistryContext<RecipeSerializer<?>> SERIALIZER_CONTEXT = new RegistryContext<>(BuiltInRegistries.RECIPE_SERIALIZER, MoreThanAFoxbox.ID);

	public static final Supplier<IdentifiableRecipeType<ContainerRecipeInput, SewingTableRecipe>> SEWING_TYPE = TYPE_CONTEXT.defer("sewing", IdentifiableRecipeType::new);
	public static final Supplier<SimpleRecipeSerializer<SewingTableRecipe>> SEWING_SERIALIZER = SERIALIZER_CONTEXT.defer(
		"sewing",
		key -> new SimpleRecipeSerializer<>(
			SewingTableRecipe.CODEC,
			SewingTableRecipe.STREAM_CODEC
		)
	);

	public static void register() {
		TYPE_CONTEXT.register();
		SERIALIZER_CONTEXT.register();
	}

	private MtfbRecipes() {}
}
