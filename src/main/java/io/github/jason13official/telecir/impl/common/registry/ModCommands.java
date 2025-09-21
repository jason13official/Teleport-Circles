package io.github.jason13official.telecir.impl.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jason13official.telecir.impl.common.entity.TeleportCircle;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;

public class ModCommands {

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
      CommandBuildContext buildContext, Commands.CommandSelection selection) {

//    dispatcher.register(Commands.literal("telecir")
//        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS)).then(
//            Commands.argument("pos", BlockPosArgument.blockPos()).executes(
//                commandContext -> createNewTeleportCircle(commandContext.getSource(),
//                    BlockPosArgument.getLoadedBlockPos(commandContext, "pos")))));

    // do nothing
    dispatcher.register(Commands.literal("telecir")
        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS)).then(
            Commands.argument("pos", BlockPosArgument.blockPos()).executes(
                commandContext -> 1)));
  }

//  private static int createNewTeleportCircle(CommandSourceStack source, BlockPos targetPos) {
//    // return TeleportCircle.createNew(source.getLevel(), targetPos, source.getPosition());
//
//    TeleportCircle circle = new TeleportCircle(source.getLevel());
//    circle.absRotateTo(0, 0);
//    circle.moveTo(BlockPos.containing(source.getPosition()).getBottomCenter());
//    source.getLevel().addFreshEntity(circle);
//
//    return 1;
//  }
}
