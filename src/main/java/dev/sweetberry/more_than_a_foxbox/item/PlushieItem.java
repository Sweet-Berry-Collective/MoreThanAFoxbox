/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.item;

import dev.sweetberry.more_than_a_foxbox.component.MtfbComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlushieItem extends Item {
	public PlushieItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull Component getName(ItemStack stack) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return super.getName(stack);

		var location = plushie.variant().location();

		return Component.translatable("item.more_than_a_foxbox.plushie." + location.toLanguageKey());
	}
}
