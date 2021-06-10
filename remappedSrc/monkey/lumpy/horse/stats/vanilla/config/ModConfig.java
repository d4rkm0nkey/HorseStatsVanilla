package monkey.lumpy.horse.stats.vanilla.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig.ColorObj;

@Config(name = "horsestatsvanilla")
public class ModConfig implements ConfigData {
    static class ColorObj {
        public ColorObj(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
        @ConfigEntry.BoundedDiscrete(max=255)
        int red;
        @ConfigEntry.BoundedDiscrete(max=255)
        int green;
        @ConfigEntry.BoundedDiscrete(max=255)
        int blue;

        Color getColor() {
            return Color.ofRGB(red,green,blue);
        }
    }
    @ConfigEntry.Gui.Tooltip
    private boolean useColors = true;
    @ConfigEntry.Gui.Tooltip
    private boolean showMaxMin = true;
    @ConfigEntry.Gui.Tooltip
    private boolean enableTooltip = true;

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

    
    @ConfigEntry.Gui.CollapsibleObject
    private ColorObj goodColor = new ColorObj(0,180,0);
    @ConfigEntry.Gui.CollapsibleObject
    private ColorObj neutralColor = new ColorObj(70,70,70);
    @ConfigEntry.Gui.CollapsibleObject
    private ColorObj badColor = new ColorObj(255,0,0);

    public boolean useColors() {
        return useColors;
    }

    public boolean showMaxMin() {
        return showMaxMin;
    }

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
        return goodColor.getColor();
    }

    public Color getNeutralColor() {
        return neutralColor.getColor();
    }

    public Color getBadColor() {
        return badColor.getColor();
    }
}