/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.item;

import java.util.Optional;
import java.util.function.Consumer;

import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.data.PlushieDataComponent;
import org.jetbrains.annotations.NotNull;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class PlushieItem extends BlockItem {
	public PlushieItem(Properties properties) {
		super(MtfbBlocks.PLUSHIE.get(), properties);
	}

	@Override
	public @NotNull Component getName(ItemStack stack) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return super.getName(stack);

		return plushie.getDisplayName();
	}

	public Optional<SoundEvent> getInteractionSound(RegistryAccess access, ItemStack stack) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return Optional.empty();

		return plushie.getInteractionSound(access);
	}

	@Override
	public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
		var sound = getInteractionSound(level.registryAccess(), player.getItemInHand(hand));

		if (sound.isEmpty())
			return super.use(level, player, hand);

		var center = player.getEyePosition();

		level.playPlayerSound(sound.get(), SoundSource.PLAYERS, 1f, 1f);

		if (level.isClientSide())
			return InteractionResult.SUCCESS;

		level.playSeededSound(player, center.x, center.y, center.z, sound.get(), SoundSource.PLAYERS, 1f, 1f,  level.random.nextLong());

		return InteractionResult.SUCCESS;
	}

	private static MutableComponent getSoundComponent(Optional<PlushieDataComponent.SoundType> type) {
		if (type.isEmpty())
			return Component.translatable("item.more_than_a_foxbox.plushie.no_sounds");
		if (type.get() == PlushieDataComponent.SoundType.SPEAKER)
			return Component.translatable("item.more_than_a_foxbox.plushie.speaker");
		return Component.translatable("item.more_than_a_foxbox.plushie.squeaker");
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return;

		tooltipAdder.accept(getSoundComponent(plushie.soundType()).withStyle(ChatFormatting.DARK_PURPLE));

		if (flag.isAdvanced()) {
			tooltipAdder.accept(Component.empty());
			tooltipAdder.accept(
				Component
					.translatable("item.more_than_a_foxbox.plushie.debug",
						plushie
							.variant()
							.identifier()
							.toString()
					)
					.withStyle(
						ChatFormatting.DARK_GRAY
					)
			);
			tooltipAdder.accept(Component.empty());
		}
	}
}
