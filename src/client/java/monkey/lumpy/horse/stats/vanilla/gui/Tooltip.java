package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;

/** The panel content for {@link ToolTipGui}: one row per stat. */
public class Tooltip extends LightweightGuiDescription {
    public Tooltip(StatLines stats) {
        super();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5,5,0,5));

        for (StatLines.Line line : stats.getLines()) {
            WBox box = new WBox(Axis.HORIZONTAL);
            box.add(new WLabel(Text.literal(line.stat().symbol()), line.color()));
            box.add(new WLabel(Text.literal(line.value()), line.color()));
            root.add(box);
        }
        root.validate(this);
    }
}
