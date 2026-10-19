package monkey.lumpy.horse.stats.vanilla.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import monkey.lumpy.horse.stats.vanilla.HorseStatsVanilla;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;

/** The stats of a mount as rows (symbol, value, color, vanilla range), shared by the inventory and {@link ToolTipGui}. */
public final class StatLines {
    public enum Stat {
        SPEED("➟"), JUMP("⇮"), HEALTH("♥"), STRENGTH("▦");

        private final String symbol;

        Stat(String symbol) {
            this.symbol = symbol;
        }

        public String symbol() {
            return symbol;
        }
    }

    /** {@code range} is the span vanilla rolls the stat in for this kind of mount, or null if it is fixed. */
    public record Line(Stat stat, String value, int color, String range) {}

    // Spans vanilla rolls new mounts in (AbstractHorse.generateSpeed/generateJumpStrength/generateMaxHealth, Llama.setRandomStrength)
    private static final double MIN_SPEED = 0.1125, MAX_SPEED = 0.3375;
    private static final double MIN_JUMP = 0.4, MAX_JUMP = 1.0;
    private static final double MIN_HEALTH = 15, MAX_HEALTH = 30;
    private static final double MIN_LLAMA_SLOTS = 3, MAX_LLAMA_SLOTS = 15;

    private final ModConfig config = HorseStatsVanilla.config();
    private final List<Line> lines = new ArrayList<>();

    private StatLines() {}

    /** Returns null for camels: all their stats are fixed, so there is nothing to compare. */
    public static StatLines of(AbstractHorse mount) {
        if (mount instanceof Camel) {
            return null;
        }
        boolean randomSpeed = mount instanceof Horse;
        boolean randomJump = mount instanceof Horse || mount instanceof SkeletonHorse;
        boolean randomHealth = mount instanceof Horse || mount instanceof AbstractChestedHorse;

        StatLines stats = new StatLines();
        ModConfig config = stats.config;
        stats.add(Stat.SPEED, Converter.genericSpeedToBlocPerSec(mount.getAttributeValue(Attributes.MOVEMENT_SPEED)),
            config.getGoodHorseSpeedValue(), config.getBadHorseSpeedValue(),
            randomSpeed ? range(Converter.genericSpeedToBlocPerSec(MIN_SPEED), Converter.genericSpeedToBlocPerSec(MAX_SPEED)) : null);
        stats.add(Stat.JUMP, Converter.jumpStrengthToJumpHeight(mount.getAttributeValue(Attributes.JUMP_STRENGTH)),
            config.getGoodHorseJumpValue(), config.getBadHorseJumpValue(),
            randomJump ? range(Converter.jumpStrengthToJumpHeight(MIN_JUMP), Converter.jumpStrengthToJumpHeight(MAX_JUMP)) : null);
        stats.add(Stat.HEALTH, mount.getMaxHealth(),
            config.getGoodHorseHeartsValue(), config.getBadHorseHeartsValue(),
            randomHealth ? range(MIN_HEALTH, MAX_HEALTH) : null);
        if (mount instanceof Llama llama) {
            // Inventory slots: the strength is the number of chest columns, each three slots high
            stats.add(Stat.STRENGTH, 3 * llama.getStrength(),
                config.getGoodStrengthValue(), config.getBadStrengthValue(),
                range(MIN_LLAMA_SLOTS, MAX_LLAMA_SLOTS));
        }
        return stats;
    }

    public List<Line> getLines() {
        return lines;
    }

    private void add(Stat stat, double value, double good, double bad, String range) {
        // Rate the shown (rounded) value, so a displayed 4 never counts as above a limit of 4
        double shown = Math.round(value * 10) / 10.0;
        lines.add(new Line(stat, format(shown), rate(shown, good, bad), range));
    }

    private int rate(double value, double good, double bad) {
        if (config.useColors()) {
            if (value > good) {
                return config.getGoodColor().getColor();
            }
            if (value < bad) {
                return config.getBadColor().getColor();
            }
        }
        return config.getNeutralColor().getColor();
    }

    private static String range(double min, double max) {
        return format(min) + "-" + format(max);
    }

    private static String format(double value) {
        return new DecimalFormat("#.#").format(value);
    }
}
