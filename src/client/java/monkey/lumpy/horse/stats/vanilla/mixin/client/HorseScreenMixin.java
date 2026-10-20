package monkey.lumpy.horse.stats.vanilla.mixin.client;

import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MountScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends MountScreen<HorseScreenHandler> {
    // Positions per stat, in StatLines.Stat order (speed, jump, health, strength)
    @Unique
    private static final int[] TOP_ROW_X = {87, 122, 147, 62};
    @Unique
    private static final int[] TOP_ROW_X_CHEST = {80, 115, 140, 0};
    @Unique
    private static final int[] COLUMN_SYMBOL_X = {82, 84, 83, 83};

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title, int slotColumnCount, LivingEntity mount) {
        super(handler, inventory, title, slotColumnCount, mount);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        ModConfig config = StatLines.config();
        if (!config.showValue() || !(this.mount instanceof AbstractHorseEntity entity)) {
            return;
        }
        StatLines stats = StatLines.of(entity);
        if (stats == null) {
            return;
        }
        // A chest inventory covers the space next to the horse, so the values move up into the title row
        boolean hasChest = entity instanceof AbstractDonkeyEntity donkey && donkey.hasChest();
        int neutralColor = config.getNeutralColor();
        // The labels are drawn relative to the window, the mouse position is not
        int localX = mouseX - this.x;
        int localY = mouseY - this.y;
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
                width = textRenderer.getWidth(text);
                drawContext.drawText(textRenderer, text, x, y, line.color(), false);
                // Without a chest the space next to the horse is free for the ranges
                if (showRange && !hasChest) {
                    drawContext.drawText(textRenderer, symbol + " (" + line.range() + ")", COLUMN_SYMBOL_X[i], 26 + 10 * i, neutralColor, false);
                }
            } else {
                x = COLUMN_SYMBOL_X[i];
                y = 26 + 10 * i;
                width = 93 + textRenderer.getWidth(line.value()) - x;
                drawContext.drawText(textRenderer, symbol, x, y, line.color(), false);
                drawContext.drawText(textRenderer, line.value(), 93, y, line.color(), false);
                if (showRange) {
                    drawContext.drawText(textRenderer, "(" + line.range() + ")", 119, y, neutralColor, false);
                }
            }
            if (localX >= x && localX < x + width && localY >= y - 1 && localY < y + 9) {
                hovered = line.stat();
            }
        }
        if (hovered != null) {
            drawContext.drawTooltip(textRenderer, stats.getTooltip(hovered), Optional.empty(), mouseX, mouseY);
        }
    }
}
