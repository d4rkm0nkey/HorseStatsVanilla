package monkey.lumpy.horse.stats.vanilla;

import java.nio.file.Path;

import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

/** Loader-independent setup, called from the Fabric and NeoForge entrypoints. */
public final class HorseStatsVanilla {
    public static final String MOD_ID = "horsestatsvanilla";

    private HorseStatsVanilla() {}

    public static void init(Path configDir) {
        ModConfig.load(configDir);

        // Development check: apply all mixins right away instead of when their target
        // class is first used, so broken targets show up without opening a horse screen
        if (Boolean.getBoolean("horsestatsvanilla.mixinAudit")) {
            MixinEnvironment.getCurrentEnvironment().audit();
        }
    }

    public static ModConfig config() {
        return ModConfig.get();
    }
}
