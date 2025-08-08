/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.registry;

import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MtfbComponents {
	public static final DataComponentType<Holder<PlushieVariant>> PLUSHIE = create(holderBuilder -> holderBuilder
		.persistent(PlushieVariant.CODEC)
		.networkSynchronized(PlushieVariant.STREAM_CODEC)
	);

	private static <T> DataComponentType<T> create(UnaryOperator<DataComponentType.Builder<T>> consumer) {
		DataComponentType.Builder<T> componentType = DataComponentType.builder();
		consumer.apply(componentType);
		return componentType.build();
	}
}
