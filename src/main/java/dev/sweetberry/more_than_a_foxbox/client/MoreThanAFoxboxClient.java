/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client;

import dev.sweetberry.more_than_a_foxbox.client.util.PlushieModelUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.resources.ResourceLocation;

public class MoreThanAFoxboxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PreparableModelLoadingPlugin.register(PlushieModelUtil::getModels, (data, pluginContext) -> {
			for (ResourceLocation id : data) {
				pluginContext.addModel(ExtraModelKey.create(id::toString), SimpleUnbakedExtraModel.blockStateModel(id));
			}
		});
	}
}
