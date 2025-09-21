package io.github.jason13official.telecir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

  public static final String MOD_ID = "telecir";
  public static final String MOD_NAME = "Teleport Circles";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

  @SuppressWarnings("all")
  public static void debug(String message, Object obj1, Object obj2) {
    LOG.info("[Debug] " + message, obj1, obj2);
  }

  @SuppressWarnings("all")
  public static void debug(String message, Object... varargs) {
    LOG.info("[Debug] " + message, varargs);
  }

  @SuppressWarnings("all")
  public static void debug(String message) {
    LOG.info("[Debug] " + message);
  }
}
