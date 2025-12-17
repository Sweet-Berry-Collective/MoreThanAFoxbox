/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.sound;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public final class MtfbSounds {
	private static final RegistryContext<SoundEvent> CONTEXT = new RegistryContext<>(BuiltInRegistries.SOUND_EVENT, MoreThanAFoxbox.ID);

	public static final Supplier<SoundEvent> SQUEAK = CONTEXT.defer(
		"plushie.squeak",
		withVariableRange()
	);

	private MtfbSounds() {}

	public static Function<ResourceKey<SoundEvent>, SoundEvent> withVariableRange() {
		return key -> SoundEvent.createVariableRangeEvent(key.identifier());
	}

	public static void register() {
		CONTEXT.register();
	}
}
