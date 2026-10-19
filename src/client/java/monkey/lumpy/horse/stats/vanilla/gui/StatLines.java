package monkey.lumpy.horse.stats.vanilla.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;

/** The stats of a mount as rows (symbol, value, color, vanilla range), shared by the inventory and the tooltip. */
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

    // Spans vanilla rolls new mounts in (AbstractHorseEntity.getChildMovementSpeed etc., LlamaEntity.initializeStrength)
    private static final double MIN_SPEED = 0.1125, MAX_SPEED = 0.3375;
    private static final double MIN_JUMP = 0.4, MAX_JUMP = 1.0;
    private static final double MIN_HEALTH = 15, MAX_HEALTH = 30;
    private static final double MIN_LLAMA_SLOTS = 3, MAX_LLAMA_SLOTS = 15;

    private final ModConfig config = config();
    private final List<Line> lines = new ArrayList<>();

    private StatLines() {}

    public static ModConfig config() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    /** Returns null for camels: all their stats are fixed, so there is nothing to compare. */
    public static StatLines of(AbstractHorseEntity mount) {
        if (mount instanceof CamelEntity) {
            return null;
        }
        boolean randomSpeed = mount instanceof HorseEntity;
        boolean randomJump = mount instanceof HorseEntity || mount instanceof SkeletonHorseEntity;
        boolean randomHealth = mount instanceof HorseEntity || mount instanceof AbstractDonkeyEntity;

        StatLines stats = new StatLines();
        ModConfig config = stats.config;
        stats.add(Stat.SPEED, Converter.genericSpeedToBlocPerSec(mount.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)),
            config.getGoodHorseSpeedValue(), config.getBadHorseSpeedValue(),
            randomSpeed ? range(Converter.genericSpeedToBlocPerSec(MIN_SPEED), Converter.genericSpeedToBlocPerSec(MAX_SPEED)) : null);
        stats.add(Stat.JUMP, Converter.jumpStrengthToJumpHeight(mount.getAttributeValue(EntityAttributes.GENERIC_JUMP_STRENGTH)),
            config.getGoodHorseJumpValue(), config.getBadHorseJumpValue(),
            randomJump ? range(Converter.jumpStrengthToJumpHeight(MIN_JUMP), Converter.jumpStrengthToJumpHeight(MAX_JUMP)) : null);
        stats.add(Stat.HEALTH, mount.getMaxHealth(),
            config.getGoodHorseHeartsValue(), config.getBadHorseHeartsValue(),
            randomHealth ? range(MIN_HEALTH, MAX_HEALTH) : null);
        if (mount instanceof LlamaEntity llama) {
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
