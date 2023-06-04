package monkey.lumpy.horse.stats.vanilla.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.math.Color;

@Config(name = "horsestatsvanilla")
public class ModConfig implements ConfigData {

    private boolean showValue = true;
    @ConfigEntry.Gui.Tooltip
    private boolean showMaxMin = false;
    @ConfigEntry.Gui.Tooltip
    private boolean enableTooltip = true;
    private boolean valueUp = false;
    @ConfigEntry.Gui.Tooltip
    private boolean useColors = true;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    private int goodColor = 0xff00b400;
    @ConfigEntry.ColorPicker(allowAlpha = true)
    private int neutralColor = 0xff464646;
    @ConfigEntry.ColorPicker(allowAlpha = true)
    private int badColor = 0xffff0000;

    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float goodHorseJumpValue = 4;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float badHorseJumpValue = 2.5f;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float goodHorseSpeedValue = 11;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float badHorseSpeedValue = 7;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float goodHorseHeartsValue = 25;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float badHorseHeartsValue = 20;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float goodStrengthValue = 9;
    @Category("coloring")
    @ConfigEntry.Gui.Tooltip
    private float badStrengthValue = 6;

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

    public Color getGoodColor() {
        return Color.ofOpaque(goodColor);
    }

    public Color getNeutralColor() {
        return Color.ofOpaque(neutralColor);
    }

    public Color getBadColor() {
        return Color.ofOpaque(badColor);
    }
}
