package monkey.lumpy.horse.stats.vanilla;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class HorseStatsVanillaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HorseStatsVanilla.init(FabricLoader.getInstance().getConfigDir());
    }
}
