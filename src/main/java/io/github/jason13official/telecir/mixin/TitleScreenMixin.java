package io.github.jason13official.telecir.mixin;

import io.github.jason13official.telecir.Constants;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

  @Inject(at = @At("HEAD"), method = "init()V")
  private void telecir$init(CallbackInfo info) {
		Constants.LOG.info("TitleScreen#init called at {}", System.currentTimeMillis());
  }
}