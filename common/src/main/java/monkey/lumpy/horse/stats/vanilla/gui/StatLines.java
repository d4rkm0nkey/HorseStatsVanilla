package monkey.lumpy.horse.stats.vanilla.gui;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;

/** The rows (symbol, value, color) shown by {@link ToolTipGui}. */
public abstract class StatLines {
    public record Line(String symbol, String value, int color) {}

    protected final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private final List<Line> lines = new ArrayList<>();

    public List<Line> getLines() {
        return lines;
    }

    protected void add(String symbol, Object value, Color color) {
        lines.add(new Line(symbol, String.valueOf(value), color.hashCode()));
    }

    protected Color rate(double value, double good, double bad) {
        if (!config.useColors()) {
            return config.getNeutralColor();
        }
        if (value > good) {
            return config.getGoodColor();
        }
        if (value < bad) {
            return config.getBadColor();
        }
        return config.getNeutralColor();
    }
}
