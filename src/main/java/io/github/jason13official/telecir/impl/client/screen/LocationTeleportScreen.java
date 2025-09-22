package io.github.jason13official.telecir.impl.client.screen;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.impl.common.network.packet.EntityRenameC2SPacket;
import io.github.jason13official.telecir.impl.common.network.packet.TeleportC2SPacket;
import java.util.UUID;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class LocationTeleportScreen extends Screen {

  private static final int SEARCH_BAR_WIDTH = 150;
  private static final int SEARCH_BAR_HEIGHT = 20;
  private static final int SEARCH_BAR_Y_OFFSET = -6;

  private static final int HEADER_HEIGHT_SMALL = 55;
  public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
  public final UUID circleUUID;
  private final Screen lastScreen;
  private LocationList locationList;
  private EditBox searchField;
  private int searchFieldX;
  private int searchFieldY;
  private int searchFieldWidth;
  private String currentSearchQuery;

  public LocationTeleportScreen(UUID uuid, String name) {
    super(Component.literal(Constants.MOD_NAME));
    this.lastScreen = null;
    this.circleUUID = uuid;
    this.currentSearchQuery = name;
  }

  @Override
  protected void init() {
    // super.init();
    calculateSearchFieldPosition();
    this.addTitle();
    this.addContents();
    this.addFooter();
    this.layout.visitWidgets(widget -> {
      if (widget instanceof LocationList) {
        AbstractWidget abstractWidget = this.addRenderableWidget(widget);
      }
    });
    this.repositionElements();


  }

  private void calculateSearchFieldPosition() {
    this.searchFieldY = 10; // HEADER_HEIGHT_SMALL - SEARCH_BAR_HEIGHT + SEARCH_BAR_Y_OFFSET;
    this.searchFieldWidth = Math.min(SEARCH_BAR_WIDTH, this.width - 40);
    this.searchFieldX = (this.width - this.searchFieldWidth) / 2;
  }

  protected void addTitle() {
    this.layout.addTitleHeader(GameNarrator.NO_TITLE, this.font);
    this.searchField = new EditBox(this.font, this.searchFieldX + 5, this.searchFieldY,
        this.searchFieldWidth, SEARCH_BAR_HEIGHT, Component.literal("Circle Name"));
    this.searchField.setBordered(false);
    this.searchField.setValue(this.currentSearchQuery);
    this.searchField.setResponder(this::onSearchChanged);
    this.addWidget(this.searchField);
  }

  private void onSearchChanged(String name) {
    this.currentSearchQuery = name;
    EntityRenameC2SPacket.createAndSend(this.circleUUID, this.currentSearchQuery);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

    assert this.minecraft != null;
    assert this.minecraft.player != null;

    if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
      this.minecraft.setScreen(null);
      return true;
    }

    return this.searchField.keyPressed(keyCode, scanCode, modifiers)
        || this.searchField.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    this.searchField.setFocused(isMouseOverSearchFieldWithPadding(mouseX, mouseY));
    return super.mouseClicked(mouseX, mouseY, button);
  }

  protected void addContents() {
    this.locationList = this.layout.addToContents(new LocationList(this, this.minecraft));
  }

  protected void addFooter() {
    this.layout.addToFooter(Button.builder(Component.literal("Close"),
        button -> this.onClose()).width(200).build());
  }

  protected void repositionElements() {
    this.layout.arrangeElements();
    this.locationList.updateSize(this.width, this.layout);
  }

  @Override
  public void onClose() {
    this.minecraft.setScreen(this.lastScreen);
  }

  public void teleportToLocation(UUID uuid, String name, Long location) {
    TeleportC2SPacket.createAndSend(location);
  }

  private boolean isMouseOverSearchFieldWithPadding(double mouseX, double mouseY) {
    int paddedX = this.searchFieldX - 5;
    int paddedY = this.searchFieldY - 5;
    return mouseX >= paddedX && mouseY >= paddedY && mouseX <= paddedX + this.searchFieldWidth
        && mouseY <= paddedY + SEARCH_BAR_HEIGHT;
  }

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);

    if (isMouseOverSearchFieldWithPadding(mouseX, mouseY)) {
      int outlineX = (this.width - this.searchFieldWidth) / 2;
      int outlineY = this.searchFieldY - 6;
      guiGraphics.renderOutline(outlineX, outlineY, SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT,
          0xAAFFFFFF);
    }

    if (this.searchField.isFocused()) {
      this.searchField.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    else {
      guiGraphics.drawCenteredString(this.font, this.currentSearchQuery, this.width / 2, 10, 0xFFFFFFFF);
    }
  }
}
