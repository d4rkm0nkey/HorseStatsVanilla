package monkey.lumpy.horse.stats.vanilla.gui;

import java.util.List;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
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
            List<Text> tooltip = stats.getTooltip(line.stat());
            // The row also covers the gap between symbol and value
            WBox box = new WBox(Axis.HORIZONTAL) {
                @Override
                public void addTooltip(TooltipBuilder builder) {
                    builder.add(tooltip.toArray(Text[]::new));
                }
            };
            box.add(new StatLabel(line.stat().symbol(), line.color(), tooltip));
            box.add(new StatLabel(line.value(), line.color(), tooltip));
            root.add(box);
        }
        root.validate(this);
    }

    /** A label that explains its stat on hover. */
    private static class StatLabel extends WLabel {
        private final List<Text> tooltip;

        StatLabel(String text, int color, List<Text> tooltip) {
            super(Text.literal(text), color);
            this.tooltip = tooltip;
        }

        @Override
        public void addTooltip(TooltipBuilder builder) {
            builder.add(tooltip.toArray(Text[]::new));
        }
    }
}
