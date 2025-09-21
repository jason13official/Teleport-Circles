package io.github.jason13official.telecir.impl.client.screen;

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

class LocationList extends ContainerObjectSelectionList<Entry> {

  private static final int ITEM_HEIGHT = 25;
  final LocationTeleportScreen screen;

  public LocationList(LocationTeleportScreen screen, Minecraft minecraft) {
    super(minecraft, screen.width, screen.layout.getContentHeight(),
        screen.layout.getHeaderHeight(), ITEM_HEIGHT);
    this.screen = screen;

    // Populate entries from the map
    for (Map.Entry<UUID, CircleRecord> entry : TeleCirClient.SYNCED_CLIENT_MAP
        .entrySet()) {

      // ensure we are not adding the same circle giving us the screen,
      // and that the added circles are activated
      if (!entry.getKey().equals(this.screen.circleUUID) && entry.getValue().activated()) {

        // ensure the destination is in the same dimension
        if (Minecraft.getInstance().level.dimension().location().toString().equalsIgnoreCase(entry.getValue().dimension())) {
          this.addEntry(new LocationEntry(entry.getKey(), entry.getValue().name(),  // Display name
              entry.getValue().position()  // Location ID
          ));
        }
      }
    }
  }

  public void refreshEntries() {
    // Clear and repopulate entries
    this.clearEntries();
    for (Map.Entry<UUID, CircleRecord> entry : TeleCirClient.SYNCED_CLIENT_MAP
        .entrySet()) {
      if (entry.getValue().activated()) {
        this.addEntry(
            new LocationEntry(entry.getKey(), entry.getValue().name(), entry.getValue().position()));
      }
    }
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
    private final Long location;
    private final Button teleportButton;

    LocationEntry(UUID uuid, String name, Long location) {
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
