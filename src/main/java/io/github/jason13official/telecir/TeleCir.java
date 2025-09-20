package io.github.jason13official.telecir;

import net.fabricmc.api.ModInitializer;

public class TeleCir implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Began common initialization.");
		Constants.LOG.info("Ended common initialization.");
	}
}