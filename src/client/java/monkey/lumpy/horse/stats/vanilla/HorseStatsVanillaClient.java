package monkey.lumpy.horse.stats.vanilla;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class HorseStatsVanillaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Load the config now, so a broken file is reported at startup
        ModConfig.get();

        // Development check: apply all mixins once the client has started instead of when
        // their target class is first used, so broken targets show up without opening a horse screen
        if (Boolean.getBoolean("horsestatsvanilla.mixinAudit")) {
            ClientLifecycleEvents.CLIENT_STARTED.register(client -> MixinEnvironment.getCurrentEnvironment().audit());
        }
    }
}