package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;

/** Loader-independent setup, called from the Fabric and NeoForge entrypoints. */
public final class HorseStatsVanilla {
    public static final String MOD_ID = "horsestatsvanilla";

    private HorseStatsVanilla() {}

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}
