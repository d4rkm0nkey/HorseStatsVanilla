package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.input.KeyEvent;

public class ToolTipGui extends CottonClientScreen {

    public ToolTipGui(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if(input.scancode() == 26) {
            onClose();
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        if(input.scancode() == 50) {
            onClose();
        }
        return super.keyReleased(input);
    }
    
}