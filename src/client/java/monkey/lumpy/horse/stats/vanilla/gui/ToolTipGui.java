package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ToolTipGui extends CottonClientScreen {

    public ToolTipGui(GuiDescription description) {
        super(description);
    }

    /** Called when the player right clicks a mount; opens the panel on shift + right click on an untamed one. */
    public static void openOnInteract(AbstractHorseEntity mount, PlayerEntity player) {
        // In singleplayer interactMob also runs on the integrated server thread,
        // where opening a screen crashes the client
        if (!mount.getEntityWorld().isClient() || mount.isTame() || !player.shouldCancelInteraction()) {
            return;
        }
        ModConfig config = StatLines.config();
        if (!config.showValue() || !config.isTooltipEnabled()) {
            return;
        }
        StatLines stats = StatLines.of(mount);
        if (stats != null) {
            MinecraftClient.getInstance().setScreen(new ToolTipGui(new Tooltip(stats)));
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (MinecraftClient.getInstance().options.inventoryKey.matchesKey(input)) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        if (MinecraftClient.getInstance().options.sneakKey.matchesKey(input)) {
            close();
            return true;
        }
        return super.keyReleased(input);
    }
    
}