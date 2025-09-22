package io.github.jason13official.telecir.impl.common.util;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCir;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfiguration {

  public static void readOrCreateConfig() {

    String configDirectoryString = FabricLoader.getInstance().getConfigDir().toString();
    File configDirectory = new File(configDirectoryString);

    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("Insufficient permissions to create {}", configDirectoryString);
      defaulting();
    }

    String configFileString = configDirectoryString + File.separator + "telecir.txt";
    File configFile = new File(configFileString);

    if (configFile.isFile()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
        String s = reader.readLine();
        TeleCir.DEBUG = Boolean.parseBoolean(s);
      } catch (Exception e) {
        Constants.LOG.info(e.getMessage());
        Constants.LOG.info("Failed to read telecir.txt in {}", configDirectoryString);
        defaulting();
      }
    } else {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
        writer.write("false");
        writer.flush();
      } catch (Exception e) {
        Constants.LOG.info(e.getMessage());
        Constants.LOG.info("Failed to write telecir.txt in {}", configDirectoryString);
        defaulting();
      }
    }

    if (TeleCir.DEBUG) Constants.debug("Additional logging enabled!");
  }

  public static void defaulting() {
    Constants.LOG.info("Defaulting to false for TeleCir.DEBUG boolean.");
  }
}
