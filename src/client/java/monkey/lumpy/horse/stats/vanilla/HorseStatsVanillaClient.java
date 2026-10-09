package monkey.lumpy.horse.stats.vanilla;

import net.fabricmc.api.ClientModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class HorseStatsVanillaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        // Development check: apply all mixins right away instead of when their target
        // class is first used, so broken targets show up without opening a horse screen
        if (Boolean.getBoolean("horsestatsvanilla.mixinAudit")) {
            MixinEnvironment.getCurrentEnvironment().audit();
        }
    }
}