package monkey.lumpy.horse.stats.vanilla.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

/** Small panel with the stats of an untamed horse, shown on shift + right click. */
public class ToolTipGui extends Screen {
    private static final int PADDING = 5;
    private static final int LINE_HEIGHT = 10;
    private static final int SYMBOL_WIDTH = 11;

    private final StatLines stats;

    public ToolTipGui(StatLines stats) {
        super(Component.empty());
        this.stats = stats;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int valueWidth = 0;
        for (StatLines.Line line : stats.getLines()) {
            valueWidth = Math.max(valueWidth, font.width(line.value()));
        }
        int panelWidth = PADDING * 2 + SYMBOL_WIDTH + valueWidth;
        int panelHeight = PADDING * 2 + stats.getLines().size() * LINE_HEIGHT - 2;
        int x = (width - panelWidth) / 2;
        int y = (height - panelHeight) / 2;

        drawPanel(graphics, x, y, panelWidth, panelHeight);
        int lineY = y + PADDING;
        for (StatLines.Line line : stats.getLines()) {
            graphics.text(font, line.symbol(), x + PADDING, lineY, line.color(), false);
            graphics.text(font, line.value(), x + PADDING + SYMBOL_WIDTH, lineY, line.color(), false);
            lineY += LINE_HEIGHT;
        }
    }

    // Vanilla-style light gray panel with a beveled border
    private static void drawPanel(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        graphics.fill(x + 1, y, x + w - 1, y + h, 0xFF000000);
        graphics.fill(x, y + 1, x + w, y + h - 1, 0xFF000000);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFFC6C6C6);
        graphics.fill(x + 1, y + 1, x + w - 2, y + 2, 0xFFFFFFFF);
        graphics.fill(x + 1, y + 1, x + 2, y + h - 2, 0xFFFFFFFF);
        graphics.fill(x + 2, y + h - 2, x + w - 1, y + h - 1, 0xFF555555);
        graphics.fill(x + w - 2, y + 2, x + w - 1, y + h - 1, 0xFF555555);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (Minecraft.getInstance().options.keyInventory.matches(input)) {
            onClose();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        if (Minecraft.getInstance().options.keyShift.matches(input)) {
            onClose();
            return true;
        }
        return super.keyReleased(input);
    }
}
