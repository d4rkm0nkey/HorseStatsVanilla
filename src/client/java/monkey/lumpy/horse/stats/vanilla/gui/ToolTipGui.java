package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.input.KeyInput;

public class ToolTipGui extends CottonClientScreen {

    public ToolTipGui(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if(input.scancode() == 26) {
            close();
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        if(input.scancode() == 50) {
            close();
        }
        return super.keyReleased(input);
    }
    
}