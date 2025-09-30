package io.github.jason13official.telecir.impl.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.jason13official.telecir.TeleCir;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
      CommandBuildContext buildContext, Commands.CommandSelection selection) {

    dispatcher.register(Commands.literal("telecir")
        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
        .executes(ModCommands::toggleDebugging));
  }

  private static int toggleDebugging(CommandContext<CommandSourceStack> source) {
    TeleCir.DEBUG = !TeleCir.DEBUG;
    return 1;
  }
}
