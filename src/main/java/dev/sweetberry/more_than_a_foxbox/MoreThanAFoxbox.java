/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox;

import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.block.entity.MtfbBlockEntityTypes;
import dev.sweetberry.more_than_a_foxbox.item.MtfbItems;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.menu.MtfbMenus;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import dev.sweetberry.more_than_a_foxbox.sound.MtfbSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreThanAFoxbox implements ModInitializer {
	public static final String ID = "more_than_a_foxbox";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		MtfbRegistries.register();
		MtfbBlocks.register();
		MtfbBlockEntityTypes.register();
		MtfbItems.register();
		MtfbComponents.register();
		MtfbSounds.register();
		MtfbMenus.register();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
}
