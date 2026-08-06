package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class HorseStatsVanillaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}
