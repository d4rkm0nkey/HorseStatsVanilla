package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

/** Loader-independent setup, called from the Fabric and NeoForge entrypoints. */
public final class HorseStatsVanilla {
    public static final String MOD_ID = "horsestatsvanilla";

    private HorseStatsVanilla() {}

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        // Development check: apply all mixins right away instead of when their target
        // class is first used, so broken targets show up without opening a horse screen
        if (Boolean.getBoolean("horsestatsvanilla.mixinAudit")) {
            MixinEnvironment.getCurrentEnvironment().audit();
        }
    }
}
