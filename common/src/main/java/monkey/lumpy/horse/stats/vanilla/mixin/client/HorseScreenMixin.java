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

import java.util.Optional;

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
        // The labels are drawn relative to the window, the mouse position is not
        int localX = mouseX - leftPos;
        int localY = mouseY - topPos;
        StatLines.Stat hovered = null;

        for (StatLines.Line line : stats.getLines()) {
            int i = line.stat().ordinal();
            String symbol = line.stat().symbol();
            boolean showRange = config.showMaxMin() && line.range() != null;
            if (line.stat() == StatLines.Stat.STRENGTH && hasChest) {
                continue;
            }

            int x;
            int y;
            int width;
            if (config.valueUp() || hasChest) {
                String text = symbol + " " + line.value();
                x = (config.valueUp() ? TOP_ROW_X : TOP_ROW_X_CHEST)[i];
                y = 6;
                width = font.width(text);
                drawContext.text(font, text, x, y, line.color(), false);
                // Without a chest the space next to the horse is free for the ranges
                if (showRange && !hasChest) {
                    drawContext.text(font, symbol + " (" + line.range() + ")", COLUMN_SYMBOL_X[i], 26 + 10 * i, neutralColor, false);
                }
            } else {
                x = COLUMN_SYMBOL_X[i];
                y = 26 + 10 * i;
                width = 93 + font.width(line.value()) - x;
                drawContext.text(font, symbol, x, y, line.color(), false);
                drawContext.text(font, line.value(), 93, y, line.color(), false);
                if (showRange) {
                    drawContext.text(font, "(" + line.range() + ")", 119, y, neutralColor, false);
                }
            }
            if (localX >= x && localX < x + width && localY >= y - 1 && localY < y + 9) {
                hovered = line.stat();
            }
        }
        if (hovered != null) {
            drawContext.setTooltipForNextFrame(font, stats.getTooltip(hovered), Optional.empty(), mouseX, mouseY);
        }
    }
}
