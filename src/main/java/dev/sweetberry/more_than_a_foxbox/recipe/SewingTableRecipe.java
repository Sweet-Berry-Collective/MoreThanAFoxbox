/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.recipe;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record SewingTableRecipe(
	Ingredient shell,
	Ingredient filler,
	Ingredient essence,
	Optional<Ingredient> upgrade,
	ItemStack result
) implements Recipe<ContainerRecipeInput> {
	public static final MapCodec<SewingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst
		.group(
			Ingredient.CODEC.fieldOf("shell").forGetter(SewingTableRecipe::shell),
			Ingredient.CODEC.fieldOf("filler").forGetter(SewingTableRecipe::filler),
			Ingredient.CODEC.fieldOf("essence").forGetter(SewingTableRecipe::essence),
			Ingredient.CODEC.optionalFieldOf("upgrade").forGetter(SewingTableRecipe::upgrade),
			ItemStack.CODEC.fieldOf("result").forGetter(SewingTableRecipe::result)
		)
		.apply(inst, SewingTableRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, SewingTableRecipe> STREAM_CODEC = StreamCodec.composite(
		Ingredient.CONTENTS_STREAM_CODEC, SewingTableRecipe::shell,
		Ingredient.CONTENTS_STREAM_CODEC, SewingTableRecipe::filler,
		Ingredient.CONTENTS_STREAM_CODEC, SewingTableRecipe::essence,
		Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, SewingTableRecipe::upgrade,
		ItemStack.STREAM_CODEC, SewingTableRecipe::result,
		SewingTableRecipe::new
	);

	private  boolean testUpgrade(ItemStack stack) {
		return upgrade.map(value -> value.test(stack)).orElseGet(stack::isEmpty);
	}

	@Override
	public boolean matches(ContainerRecipeInput input, Level level) {
		if (!shell.test(input.getItem(0)))
			return false;

		if (!filler.test(input.getItem(1)))
			return false;

		if (!essence.test(input.getItem(2)))
			return false;

		return testUpgrade(input.getItem(3));
	}

	@Override
	public @NotNull ItemStack assemble(ContainerRecipeInput input, HolderLookup.Provider registries) {
		return result.copy();
	}

	@Override
	public @NotNull RecipeSerializer<? extends Recipe<ContainerRecipeInput>> getSerializer() {
		return MtfbRecipes.SEWING_SERIALIZER.get();
	}

	@Override
	public @NotNull RecipeType<? extends Recipe<ContainerRecipeInput>> getType() {
		return MtfbRecipes.SEWING_TYPE.get();
	}

	@Override
	public @NotNull PlacementInfo placementInfo() {
		return upgrade.map(ingredient -> PlacementInfo.create(
			List.of(
				shell,
				filler,
				ingredient
			)
		)).orElseGet(() -> PlacementInfo.create(
			List.of(
				shell,
				filler
			)
		));
	}

	@Override
	public @NotNull RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.STONECUTTER;
	}
}
