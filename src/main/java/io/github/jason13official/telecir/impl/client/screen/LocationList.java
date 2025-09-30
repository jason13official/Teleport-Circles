package io.github.jason13official.telecir.impl.client.screen;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.TeleCirClient;
import io.github.jason13official.telecir.impl.client.screen.LocationList.Entry;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import oshi.util.tuples.Triplet;

public class LocationList extends ContainerObjectSelectionList<Entry> {

  private static final int ITEM_HEIGHT = 25;
  final LocationTeleportScreen screen;

  public LocationList(LocationTeleportScreen screen, Minecraft minecraft) {
    super(minecraft, screen.width, screen.layout.getContentHeight(),
        screen.layout.getHeaderHeight(), ITEM_HEIGHT);
    this.screen = screen;

    populateEntries();
  }

  private void populateEntries() {
    // Validate synchronizedRecords state
    if (TeleCirClient.synchronizedRecords == null || TeleCirClient.synchronizedRecords.isEmpty()) {
      Constants.debug("synchronizedRecords is empty during LocationList population");
      return;
    }

    Constants.debug("LocationList population: synchronizedRecords size = {}", TeleCirClient.synchronizedRecords.size());
    Constants.debug("Current dimension: {}", getCurrentDimension());
    Constants.debug("Current circle UUID: {}", this.screen.circleUUID);

    // Populate entries from the map
    for (Map.Entry<UUID, CircleRecord> entry : TeleCirClient.synchronizedRecords.entrySet()) {
      Constants.debug("iterated over {} named {}", entry.getKey().toString(), entry.getValue().name());

      if (shouldIncludeEntry(entry.getKey(), entry.getValue())) {
        this.addEntry(new LocationEntry(entry.getKey(), entry.getValue().name(), entry.getValue().position()));
      }
    }
  }

  private boolean shouldIncludeEntry(UUID uuid, CircleRecord record) {
    // Exclude current circle
    if (uuid.equals(this.screen.circleUUID)) {
      Constants.debug("Filtered out current circle: {}", record.name());
      return false;
    }

    // Only activated circles
    if (!record.activated()) {
      Constants.debug("Filtered out inactive circle: {}", record.name());
      return false;
    }

    // Same dimension check
    if (!isSameDimension(record.dimension())) {
      Constants.debug("Filtered out different dimension circle: {} in {}", record.name(), record.dimension());
      return false;
    }

    return true;
  }

  private String getCurrentDimension() {
    try {
      return Minecraft.getInstance().level.dimension().location().toString();
    } catch (Exception e) {
      Constants.debug("Error getting current dimension: {}", e.getMessage());
      return "";
    }
  }

  private boolean isSameDimension(String recordDimension) {
    try {
      String currentDimension = getCurrentDimension();
      return java.util.Objects.equals(currentDimension, recordDimension);
    } catch (Exception e) {
      Constants.debug("Error comparing dimensions: {}", e.getMessage());
      return false;
    }
  }

  public void refreshEntries() {
    // Clear and repopulate entries using the same filtering logic as constructor
    this.clearEntries();
    populateEntries();
  }

  @Override
  public int getRowWidth() {
    return 340; // Same as KeyBindsList
  }

  public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {

  }

  public class LocationEntry extends Entry {

    private final UUID uuid;
    private final String name;
    private final Triplet<Double, Double, Double> location;
    private final Button teleportButton;

    LocationEntry(UUID uuid, String name, Triplet<Double, Double, Double> location) {
      this.uuid = uuid;
      this.name = name;
      this.location = location;

      this.teleportButton = Button.builder(Component.literal(name),
              button -> {
                LocationList.this.screen.teleportToLocation(uuid, name, location);
                Minecraft.getInstance().setScreen(null);
              })
          .bounds(0, 0, 150, 20).build();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height,
        int mouseX, int mouseY, boolean hovering, float partialTick) {

      int buttonY = top + (height - 20) / 2;

      // Position teleport button next to delete button (like KeyEntry's changeButton)
      int teleportButtonX = (LocationList.this.screen.width - this.teleportButton.getWidth()) / 2;
      this.teleportButton.setPosition(teleportButtonX, buttonY);
      this.teleportButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public List<? extends GuiEventListener> children() {
      return List.of(this.teleportButton);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
      return List.of(this.teleportButton);
    }
  }
}
