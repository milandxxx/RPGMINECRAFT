package com.aventura.rpg.event;

import com.aventura.rpg.profile.RpgProfile;
import com.aventura.rpg.profile.RpgProfileHolder;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class RpgGameplayEvents {
    private RpgGameplayEvents() { }

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, killer, victim) -> {
            if (killer instanceof ServerPlayerEntity player) awardKillExperience(player, victim);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 20 != 0) return;
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) profile(player).restoreMana(1);
        });
    }

    private static void awardKillExperience(ServerPlayerEntity player, Entity victim) {
        int experience = victim instanceof HostileEntity ? 25 : victim instanceof AnimalEntity ? 8 : 0;
        if (experience == 0) return;
        int levels = profile(player).addExperience(experience);
        if (levels > 0) player.sendMessage(Text.literal("Has subido al nivel " + profile(player).level() + " y recibiste " + (levels * 3) + " puntos."), false);
    }

    public static RpgProfile profile(ServerPlayerEntity player) { return ((RpgProfileHolder) player).aventuraRpg$getProfile(); }
}
