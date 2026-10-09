package monkey.lumpy.horse.stats.vanilla;

import net.fabricmc.api.ClientModInitializer;

public class HorseStatsVanillaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HorseStatsVanilla.init();
    }
}
