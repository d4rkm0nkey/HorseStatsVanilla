package monkey.lumpy.horse.stats.vanilla.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "horsestatsvanilla")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    boolean useColors = true;
    @ConfigEntry.Gui.Tooltip
    boolean showMaxMin = true;

    public boolean useColors() {
        return useColors;
    }

    public boolean showMaxMin() {
        return showMaxMin;
    }
    
}