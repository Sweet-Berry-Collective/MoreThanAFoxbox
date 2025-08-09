/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * An octal direction like on a compass.
 */
public enum OctalDirection implements StringRepresentable {
	SOUTH(0.0f),
	SOUTH_WEST(45.0f),
	WEST(90.0f),
	NORTH_WEST(135.0f),
	NORTH(180.0f),
	NORTH_EAST(225.0f),
	EAST(270.0f),
	SOUTH_EAST(315.0f),;
	
	private final float degrees;

	OctalDirection(float degrees) {
		this.degrees = degrees;
	}

	public static OctalDirection fromYRot(float degrees) {
		// https://stackoverflow.com/a/45317121 
		int index = Math.max(Math.round(((degrees + 360.0f) % 360.0f) / 45.0f) % 8, 0);
		return OctalDirection.values()[index];
	}

	public float getDegrees() {
		return this.degrees;
	}

	public OctalDirection getOpposite() {
		return OctalDirection.values()[(this.ordinal() + 4) % 8];
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
