package com.aventura.rpg;

import com.aventura.rpg.command.RpgCommands;
import com.aventura.rpg.event.RpgGameplayEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AventuraRpgMod implements ModInitializer {
    public static final String MOD_ID = "aventura_rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        RpgCommands.register();
        RpgGameplayEvents.register();
        LOGGER.info("Aventura RPG listo: progresion y supervivencia activadas.");
    }
}
