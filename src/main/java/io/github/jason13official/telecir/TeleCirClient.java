package io.github.jason13official.telecir;

import net.fabricmc.api.ClientModInitializer;

public class TeleCirClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    long start = System.currentTimeMillis();
    Constants.debug("Began client initialization.");

    Constants.debug("Ended client initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Client initialized in {}ms", total);
  }
}