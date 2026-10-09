package monkey.lumpy.horse.stats.vanilla;

import me.shedaniel.autoconfig.AutoConfigClient;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = HorseStatsVanilla.MOD_ID, dist = Dist.CLIENT)
public class HorseStatsVanillaNeoForge {
    public HorseStatsVanillaNeoForge(ModContainer container) {
        HorseStatsVanilla.init();
        // Config button in NeoForge's mod list
        container.registerExtensionPoint(IConfigScreenFactory.class,
            (mod, parent) -> AutoConfigClient.getConfigScreen(ModConfig.class, parent).get());
    }
}
