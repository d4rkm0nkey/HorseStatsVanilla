package monkey.lumpy.horse.stats.vanilla;

import monkey.lumpy.horse.stats.vanilla.config.ClothConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = HorseStatsVanilla.MOD_ID, dist = Dist.CLIENT)
public class HorseStatsVanillaNeoForge {
    public HorseStatsVanillaNeoForge(ModContainer container) {
        HorseStatsVanilla.init(FMLPaths.CONFIGDIR.get());
        // Config button in NeoForge's mod list; without Cloth Config the options are only in the config file
        if (ModList.get().isLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> ClothConfigScreen.create(parent));
        }
    }
}
