/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.item;

import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.component.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.component.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.sound.MtfbSounds;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlushieItem extends BlockItem {
	public PlushieItem(Properties properties) {
		super(MtfbBlocks.PLUSHIE.get(), properties);
	}

	@Override
	public @NotNull Component getName(ItemStack stack) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return super.getName(stack);

		var location = plushie.variant().location();

		return Component.translatable("item.more_than_a_foxbox.plushie." + location.toLanguageKey());
	}

	public Optional<SoundEvent> getSoundEvent(RegistryAccess access, ItemStack stack) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return Optional.empty();

		var maybeSoundType = plushie.soundType();

		if (maybeSoundType.isEmpty())
			return Optional.empty();

		var soundType = maybeSoundType.get();

		if (soundType == PlushieDataComponent.SoundType.SPEAKER) {
			var maybeVariant = access.get(plushie.variant());

			return maybeVariant.map(
				plushieVariantReference -> plushieVariantReference
					.value()
					.mobSounds()
					.value()
			);
		}

		return Optional.of(MtfbSounds.SQUEAK.get());
	}

	@Override
	public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
		var sound = getSoundEvent(level.registryAccess(), player.getItemInHand(hand));

		if (sound.isEmpty())
			return super.use(level, player, hand);

		level.playPlayerSound(sound.get(), SoundSource.PLAYERS, 1, 1);

		return InteractionResult.SUCCESS;
	}
}
