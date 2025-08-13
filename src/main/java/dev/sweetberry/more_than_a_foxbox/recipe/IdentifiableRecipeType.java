/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.recipe;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;

public record IdentifiableRecipeType(ResourceKey<IdentifiableRecipeType> key) implements RecipeType<SewingTableRecipe> {
	@Override
	public String toString() {
		return key.location().toString();
	}
}
