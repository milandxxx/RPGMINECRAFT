package com.aventura.rpg.command;

import com.aventura.rpg.event.RpgGameplayEvents;
import com.aventura.rpg.profile.RpgClass;
import com.aventura.rpg.profile.RpgProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class RpgCommands {
    private RpgCommands() { }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            CommandManager.literal("rpg")
                .then(CommandManager.literal("profile").executes(RpgCommands::showProfile))
                .then(CommandManager.literal("choose").then(CommandManager.argument("class", StringArgumentType.word())
                    .suggests((context, builder) -> { for (RpgClass value : RpgClass.values()) if (value != RpgClass.ADVENTURER) builder.suggest(value.name().toLowerCase()); return builder.buildFuture(); })
                    .executes(RpgCommands::chooseClass)))
                .then(CommandManager.literal("admin").requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("grantxp").then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(RpgCommands::grantExperience)))))
        ));
    }

    private static int showProfile(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;
        RpgProfile profile = RpgGameplayEvents.profile(player);
        context.getSource().sendFeedback(() -> Text.literal("RPG | Nivel " + profile.level() + " | XP " + profile.experience() + "/" + profile.experienceToNextLevel() + " | Clase " + profile.rpgClass().displayName() + " | Puntos " + profile.unspentPoints() + " | Fuerza " + profile.strength() + " | Inteligencia " + profile.intelligence() + " | Vitalidad " + profile.vitality() + " | Mana " + profile.mana() + "/" + profile.maximumMana()), false);
        return 1;
    }

    private static int chooseClass(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;
        try {
            RpgClass selected = RpgClass.valueOf(StringArgumentType.getString(context, "class").toUpperCase());
            if (RpgGameplayEvents.profile(player).chooseClass(selected)) {
                context.getSource().sendFeedback(() -> Text.literal("Has elegido la clase " + selected.displayName() + "."), false);
                return 1;
            }
        } catch (IllegalArgumentException ignored) { }
        context.getSource().sendError(Text.literal("Clase invalida o ya elegiste una. Usa guerrero, mago, arquero o picaro."));
        return 0;
    }

    private static int grantExperience(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        RpgGameplayEvents.profile(target).addExperience(amount);
        context.getSource().sendFeedback(() -> Text.literal("Otorgados " + amount + " XP a " + target.getName().getString() + "."), true);
        return 1;
    }
}
