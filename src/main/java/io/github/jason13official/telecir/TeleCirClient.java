package io.github.jason13official.telecir;

import net.fabricmc.api.ClientModInitializer;

public class TeleCirClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Constants.LOG.info("Began client initialization.");
		Constants.LOG.info("Ended client initialization.");
	}
}