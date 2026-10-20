package monkey.lumpy.horse.stats.vanilla.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.fabricmc.loader.api.FabricLoader;

/**
 * The options, stored as JSON in config/horsestatsvanilla.json5 (the name Cloth Config's
 * AutoConfig used, so existing configs keep working). The fields are package private for
 * {@link ClothConfigScreen}, the only place they are changed.
 */
public class ModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("horsestatsvanilla");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig instance;

    boolean showValue = true;
    boolean showMaxMin = false;
    boolean enableTooltip = true;
    boolean valueUp = false;
    boolean useColors = true;

    int goodColor = 0xff00b400;
    int neutralColor = 0xff464646;
    int badColor = 0xffff0000;

    float goodHorseJumpValue = 4;
    float badHorseJumpValue = 2.5f;
    float goodHorseSpeedValue = 11;
    float badHorseSpeedValue = 7;
    float goodHorseHeartsValue = 25;
    float badHorseHeartsValue = 20;
    float goodStrengthValue = 9;
    float badStrengthValue = 6;

    public static ModConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("horsestatsvanilla.json5");
    }

    private static ModConfig load() {
        Path path = path();
        if (Files.exists(path)) {
            // Options missing from the file keep their defaults
            try (Reader reader = Files.newBufferedReader(path)) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException | JsonParseException e) {
                LOGGER.error("Could not read {}, using the defaults", path, e);
                return new ModConfig();
            }
        }
        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    public void save() {
        Path path = path();
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Could not save {}", path, e);
        }
    }

    public boolean showValue() {  return showValue; }

    public boolean useColors() { return useColors; }

    public boolean showMaxMin() { return showMaxMin; }

    public boolean valueUp() { return valueUp; }

    public boolean isTooltipEnabled() {
        return enableTooltip;
    }

    public float getGoodHorseJumpValue() {
        return goodHorseJumpValue;
    }

    public float getBadHorseJumpValue() {
        return badHorseJumpValue;
    }

    public float getGoodHorseSpeedValue() {
        return goodHorseSpeedValue;
    }

    public float getBadHorseSpeedValue() {
        return badHorseSpeedValue;
    }

    public float getGoodHorseHeartsValue() {
        return goodHorseHeartsValue;
    }

    public float getBadHorseHeartsValue() {
        return badHorseHeartsValue;
    }

    public float getGoodStrengthValue() {
        return goodStrengthValue;
    }

    public float getBadStrengthValue() {
        return badStrengthValue;
    }

    // The colors are opaque ARGB, whatever alpha the file has

    public int getGoodColor() {
        return 0xff000000 | goodColor;
    }

    public int getNeutralColor() {
        return 0xff000000 | neutralColor;
    }

    public int getBadColor() {
        return 0xff000000 | badColor;
    }
}
