package monkey.lumpy.horse.stats.vanilla.mixin.client;

import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.StatLines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Shadow
    @Final
    private AbstractHorseEntity entity;

    // Positions per stat, in StatLines.Stat order (speed, jump, health, strength)
    @Unique
    private static final int[] TOP_ROW_X = {87, 122, 147, 62};
    @Unique
    private static final int[] TOP_ROW_X_CHEST = {80, 115, 140, 0};
    @Unique
    private static final int[] COLUMN_SYMBOL_X = {82, 84, 83, 83};

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        ModConfig config = StatLines.config();
        if (!config.showValue()) {
            return;
        }
        StatLines stats = StatLines.of(entity);
        if (stats == null) {
            return;
        }
        // A chest inventory covers the space next to the horse, so the values move up into the title row
        boolean hasChest = entity instanceof AbstractDonkeyEntity donkey && donkey.hasChest();
        int neutralColor = config.getNeutralColor().getColor();

        for (StatLines.Line line : stats.getLines()) {
            int i = line.stat().ordinal();
            String symbol = line.stat().symbol();
            boolean showRange = config.showMaxMin() && line.range() != null;
            if (line.stat() == StatLines.Stat.STRENGTH && hasChest) {
                continue;
            }

            if (config.valueUp()) {
                drawContext.drawText(textRenderer, symbol + " " + line.value(), TOP_ROW_X[i], 6, line.color(), false);
                if (showRange) {
                    drawContext.drawText(textRenderer, symbol + " (" + line.range() + ")", 180, 30 + 10 * i, neutralColor, false);
                }
            } else if (hasChest) {
                drawContext.drawText(textRenderer, symbol + " " + line.value(), TOP_ROW_X_CHEST[i], 6, line.color(), false);
            } else {
                int y = 26 + 10 * i;
                drawContext.drawText(textRenderer, symbol, COLUMN_SYMBOL_X[i], y, line.color(), false);
                drawContext.drawText(textRenderer, line.value(), 93, y, line.color(), false);
                if (showRange) {
                    drawContext.drawText(textRenderer, "(" + line.range() + ")", 119, y, neutralColor, false);
                }
            }
        }
    }
}
