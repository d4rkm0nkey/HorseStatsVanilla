package monkey.lumpy.horse.stats.vanilla.mixin.client;

import monkey.lumpy.horse.stats.vanilla.HorseStatsVanilla;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HorseInventoryScreen.class)
public abstract class HorseScreenMixin extends AbstractMountInventoryScreen<HorseInventoryMenu> {
    // Positions per stat, in StatLines.Stat order (speed, jump, health, strength)
    @Unique
    private static final int[] TOP_ROW_X = {87, 122, 147, 62};
    @Unique
    private static final int[] TOP_ROW_X_CHEST = {80, 115, 140, 0};
    @Unique
    private static final int[] COLUMN_SYMBOL_X = {82, 84, 83, 83};

    public HorseScreenMixin(HorseInventoryMenu handler, Inventory inventory, Component title, int inventoryColumns, LivingEntity mount) {
        super(handler, inventory, title, inventoryColumns, mount);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor drawContext, int mouseX, int mouseY) {
        super.extractLabels(drawContext, mouseX, mouseY);
        ModConfig config = HorseStatsVanilla.config();
        if (!config.showValue() || !(this.mount instanceof AbstractHorse entity)) {
            return;
        }
        StatLines stats = StatLines.of(entity);
        if (stats == null) {
            return;
        }
        // A chest inventory covers the space next to the horse, so the values move up into the title row
        boolean hasChest = entity instanceof AbstractChestedHorse chested && chested.hasChest();
        int neutralColor = config.getNeutralColor().getColor();

        for (StatLines.Line line : stats.getLines()) {
            int i = line.stat().ordinal();
            String symbol = line.stat().symbol();
            boolean showRange = config.showMaxMin() && line.range() != null;
            if (line.stat() == StatLines.Stat.STRENGTH && hasChest) {
                continue;
            }

            if (config.valueUp()) {
                drawContext.text(font, symbol + " " + line.value(), TOP_ROW_X[i], 6, line.color(), false);
                if (showRange) {
                    drawContext.text(font, symbol + " (" + line.range() + ")", 180, 30 + 10 * i, neutralColor, false);
                }
            } else if (hasChest) {
                drawContext.text(font, symbol + " " + line.value(), TOP_ROW_X_CHEST[i], 6, line.color(), false);
            } else {
                int y = 26 + 10 * i;
                drawContext.text(font, symbol, COLUMN_SYMBOL_X[i], y, line.color(), false);
                drawContext.text(font, line.value(), 93, y, line.color(), false);
                if (showRange) {
                    drawContext.text(font, "(" + line.range() + ")", 119, y, neutralColor, false);
                }
            }
        }
    }
}
