package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class ToolTipGui extends CottonClientScreen {

    public ToolTipGui(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if(keyCode == 26) {
            onClose();
        }
        return super.keyPressed(ch, keyCode, modifiers);
    }

    @Override
    public boolean keyReleased(int ch, int keyCode, int modifiers) {
        if(keyCode == 50) {
            onClose();
        }
        return super.keyReleased(ch, keyCode, modifiers);
    }
    
}