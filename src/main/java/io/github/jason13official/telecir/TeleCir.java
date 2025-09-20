package io.github.jason13official.telecir;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TeleCir implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Began common initialization.");
		ServerTickEvents.START_SERVER_TICK.register(TeleCirServer::init);
		ServerLifecycleEvents.SERVER_STOPPING.register(TeleCirServer::dereference);
		Constants.LOG.info("Ended common initialization.");
	}
}