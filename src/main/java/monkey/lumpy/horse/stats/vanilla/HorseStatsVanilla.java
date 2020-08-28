package monkey.lumpy.horse.stats.vanilla;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;

public class HorseStatsVanilla implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}