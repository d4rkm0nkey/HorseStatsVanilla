package monkey.lumpy.horse.stats.vanilla.gui;

import java.util.Optional;

import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

/** A small panel in the middle of the screen with one row (symbol, value) per stat. */
public class ToolTipGui extends Screen {
    // Layout in GUI pixels, relative to the panel
    public static final int PADDING = 5;
    public static final int ROW_HEIGHT = 10;
    public static final int VALUE_X = 27;
    private static final int MIN_VALUE_WIDTH = 18;

    private final StatLines stats;
    private final int panelWidth;
    private final int panelHeight;

    public ToolTipGui(StatLines stats) {
        super(Text.empty());
        this.stats = stats;
        int valueWidth = MIN_VALUE_WIDTH;
        for (StatLines.Line line : stats.getLines()) {
            valueWidth = Math.max(valueWidth, MinecraftClient.getInstance().textRenderer.getWidth(line.value()));
        }
        this.panelWidth = VALUE_X + valueWidth + PADDING;
        this.panelHeight = 2 * PADDING + ROW_HEIGHT * stats.getLines().size() - 2;
    }

    /** Called when the player right clicks a mount; opens the panel on shift + right click on an untamed one. */
    public static void openOnInteract(AbstractHorseEntity mount, PlayerEntity player) {
        // In singleplayer interactMob also runs on the integrated server thread,
        // where opening a screen crashes the client
        if (!mount.getWorld().isClient() || mount.isTame() || !player.shouldCancelInteraction()) {
            return;
        }
        ModConfig config = StatLines.config();
        if (!config.showValue() || !config.isTooltipEnabled()) {
            return;
        }
        StatLines stats = StatLines.of(mount);
        if (stats != null) {
            MinecraftClient.getInstance().setScreen(new ToolTipGui(stats));
        }
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        drawPanel(context, left, top, panelWidth, panelHeight);

        StatLines.Stat hovered = null;
        int y = top + PADDING;
        for (StatLines.Line line : stats.getLines()) {
            context.drawText(textRenderer, line.stat().symbol(), left + PADDING, y, line.color(), false);
            context.drawText(textRenderer, line.value(), left + VALUE_X, y, line.color(), false);
            // The row also covers the gap between symbol and value
            if (mouseX >= left + PADDING && mouseX < left + panelWidth - PADDING && mouseY >= y - 1 && mouseY < y + ROW_HEIGHT - 1) {
                hovered = line.stat();
            }
            y += ROW_HEIGHT;
        }
        if (hovered != null) {
            context.drawTooltip(textRenderer, stats.getTooltip(hovered), Optional.empty(), mouseX, mouseY);
        }
    }

    /** The light gray vanilla inventory look: black outline with rounded corners, a light top left and a dark bottom right edge. */
    private static void drawPanel(DrawContext context, int x, int y, int width, int height) {
        int right = x + width;
        int bottom = y + height;
        context.fill(x + 1, y, right - 1, bottom, 0xFF000000);
        context.fill(x, y + 1, right, bottom - 1, 0xFF000000);
        context.fill(x + 1, y + 1, right - 1, bottom - 1, 0xFFFFFFFF);
        context.fill(x + 2, y + 2, right - 1, bottom - 1, 0xFF555555);
        context.fill(x + 2, y + 2, right - 2, bottom - 2, 0xFFC6C6C6);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (MinecraftClient.getInstance().options.inventoryKey.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (MinecraftClient.getInstance().options.sneakKey.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
