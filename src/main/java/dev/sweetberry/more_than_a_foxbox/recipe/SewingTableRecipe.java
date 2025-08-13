/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record SewingTableRecipe(
	Ingredient shell,
	Ingredient filler,
	Optional<Ingredient> upgrade,
	ItemStack result
) implements Recipe<SewingTableRecipeInput> {
	public static final MapCodec<SewingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst
		.group(
			Ingredient.CODEC.fieldOf("shell").forGetter(SewingTableRecipe::shell),
			Ingredient.CODEC.fieldOf("filler").forGetter(SewingTableRecipe::filler),
			Ingredient.CODEC.optionalFieldOf("upgrade").forGetter(SewingTableRecipe::upgrade),
			ItemStack.CODEC.fieldOf("result").forGetter(SewingTableRecipe::result)
		)
		.apply(inst, SewingTableRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, SewingTableRecipe> STREAM_CODEC = StreamCodec.of(SewingTableRecipe::toNetwork, SewingTableRecipe::fromNetwork);

	public static SewingTableRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
		return new SewingTableRecipe(
			Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
			Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
			Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.decode(buf),
			ItemStack.STREAM_CODEC.decode(buf)
		);
	}

	public static void toNetwork(RegistryFriendlyByteBuf buf, SewingTableRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.shell);
		Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.filler);
		Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.encode(buf, recipe.upgrade);
		ItemStack.STREAM_CODEC.encode(buf, recipe.result);
	}

	private  boolean testUpgrade(ItemStack stack) {
		return upgrade.map(value -> value.test(stack)).orElseGet(stack::isEmpty);
	}

	@Override
	public boolean matches(SewingTableRecipeInput input, Level level) {
		if (!shell.test(input.getItem(0)))
			return false;

		if (!filler.test(input.getItem(1)))
			return false;

		return testUpgrade(input.getItem(2));
	}

	@Override
	public @NotNull ItemStack assemble(SewingTableRecipeInput input, HolderLookup.Provider registries) {
		return result.copy();
	}

	@Override
	public @NotNull RecipeSerializer<? extends Recipe<SewingTableRecipeInput>> getSerializer() {
		return MtfbRecipes.SEWING_SERIALIZER.get();
	}

	@Override
	public @NotNull RecipeType<? extends Recipe<SewingTableRecipeInput>> getType() {
		return MtfbRecipes.SEWING_TYPE.get();
	}

	@Override
	public @NotNull PlacementInfo placementInfo() {
		return PlacementInfo.create(
			List.of(
				shell,
				filler,
				upgrade
					.orElseGet(() ->
						Ingredient.of(Items.AIR)
					)
			)
		);
	}

	@Override
	public @NotNull RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.STONECUTTER;
	}
}
