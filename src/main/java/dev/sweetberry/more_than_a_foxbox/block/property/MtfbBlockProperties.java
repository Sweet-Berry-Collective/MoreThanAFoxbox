/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.property;

import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class MtfbBlockProperties {
	public static final EnumProperty<OctalDirection> FACING = EnumProperty.create("facing", OctalDirection.class);

	private MtfbBlockProperties() {}
}
