/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record ContainerRecipeInput(SimpleContainer container) implements RecipeInput {
	@Override
	public @NotNull ItemStack getItem(int index) {
		return container.getItem(index);
	}

	@Override
	public int size() {
		return container.getContainerSize();
	}
}
