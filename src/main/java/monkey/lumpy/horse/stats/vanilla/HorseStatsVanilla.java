package monkey.lumpy.horse.stats.vanilla;

import net.fabricmc.api.ModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;

public class HorseStatsVanilla implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}