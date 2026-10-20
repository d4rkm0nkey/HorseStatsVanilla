package monkey.lumpy.horse.stats.vanilla.config;

import java.util.function.Consumer;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * The options screen for Mod Menu. Cloth Config is optional, so only load this class when it is
 * installed. The translation keys are the ones AutoConfig generated.
 */
public final class ClothConfigScreen {
    private static final String PREFIX = "text.autoconfig.horsestatsvanilla.";

    private ClothConfigScreen() {}

    public static Screen create(Screen parent) {
        ModConfig config = ModConfig.get();
        ModConfig defaults = new ModConfig();
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.translatable(PREFIX + "title"))
            .setSavingRunnable(config::save);
        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable(PREFIX + "category.default"));
        general.addEntry(toggle(entries, "showValue", config.showValue, defaults.showValue, false, v -> config.showValue = v));
        general.addEntry(toggle(entries, "showMaxMin", config.showMaxMin, defaults.showMaxMin, true, v -> config.showMaxMin = v));
        general.addEntry(toggle(entries, "enableTooltip", config.enableTooltip, defaults.enableTooltip, true, v -> config.enableTooltip = v));
        general.addEntry(toggle(entries, "valueUp", config.valueUp, defaults.valueUp, false, v -> config.valueUp = v));
        general.addEntry(toggle(entries, "useColors", config.useColors, defaults.useColors, true, v -> config.useColors = v));
        general.addEntry(color(entries, "goodColor", config.goodColor, defaults.goodColor, v -> config.goodColor = v));
        general.addEntry(color(entries, "neutralColor", config.neutralColor, defaults.neutralColor, v -> config.neutralColor = v));
        general.addEntry(color(entries, "badColor", config.badColor, defaults.badColor, v -> config.badColor = v));

        ConfigCategory coloring = builder.getOrCreateCategory(Text.translatable(PREFIX + "category.coloring"));
        coloring.addEntry(limit(entries, "goodHorseJumpValue", config.goodHorseJumpValue, defaults.goodHorseJumpValue, v -> config.goodHorseJumpValue = v));
        coloring.addEntry(limit(entries, "badHorseJumpValue", config.badHorseJumpValue, defaults.badHorseJumpValue, v -> config.badHorseJumpValue = v));
        coloring.addEntry(limit(entries, "goodHorseSpeedValue", config.goodHorseSpeedValue, defaults.goodHorseSpeedValue, v -> config.goodHorseSpeedValue = v));
        coloring.addEntry(limit(entries, "badHorseSpeedValue", config.badHorseSpeedValue, defaults.badHorseSpeedValue, v -> config.badHorseSpeedValue = v));
        coloring.addEntry(limit(entries, "goodHorseHeartsValue", config.goodHorseHeartsValue, defaults.goodHorseHeartsValue, v -> config.goodHorseHeartsValue = v));
        coloring.addEntry(limit(entries, "badHorseHeartsValue", config.badHorseHeartsValue, defaults.badHorseHeartsValue, v -> config.badHorseHeartsValue = v));
        coloring.addEntry(limit(entries, "goodStrengthValue", config.goodStrengthValue, defaults.goodStrengthValue, v -> config.goodStrengthValue = v));
        coloring.addEntry(limit(entries, "badStrengthValue", config.badStrengthValue, defaults.badStrengthValue, v -> config.badStrengthValue = v));

        return builder.build();
    }

    private static AbstractConfigListEntry<?> toggle(ConfigEntryBuilder entries, String option,
            boolean value, boolean defaultValue, boolean tooltip, Consumer<Boolean> save) {
        var entry = entries.startBooleanToggle(Text.translatable(PREFIX + "option." + option), value)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(save);
        if (tooltip) {
            entry.setTooltip(Text.translatable(PREFIX + "option." + option + ".@Tooltip"));
        }
        return entry.build();
    }

    private static AbstractConfigListEntry<?> color(ConfigEntryBuilder entries, String option,
            int value, int defaultValue, Consumer<Integer> save) {
        // The picker works on RGB, the config stores opaque ARGB
        return entries.startColorField(Text.translatable(PREFIX + "option." + option), value & 0xffffff)
            .setDefaultValue(defaultValue & 0xffffff)
            .setSaveConsumer(rgb -> save.accept(0xff000000 | rgb))
            .build();
    }

    private static AbstractConfigListEntry<?> limit(ConfigEntryBuilder entries, String option,
            float value, float defaultValue, Consumer<Float> save) {
        return entries.startFloatField(Text.translatable(PREFIX + "option." + option), value)
            .setDefaultValue(defaultValue)
            .setTooltip(Text.translatable(PREFIX + "option." + option + ".@Tooltip"))
            .setSaveConsumer(save)
            .build();
    }
}
