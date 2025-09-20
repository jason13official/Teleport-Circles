package io.github.jason13official.telecir.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {


	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}
}