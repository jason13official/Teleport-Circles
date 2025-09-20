package io.github.jason13official.telecir;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeleportCircles implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Began common initialization.");
		Constants.LOG.info("Ended common initialization.");
	}
}