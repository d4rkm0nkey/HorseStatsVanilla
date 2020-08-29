package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.text.LiteralText;

public class Tooltip extends LightweightGuiDescription {
    private final int normalColor = 4210752;
    private final int badColor = 16733525;
    private final int goodColor = 43520;
    private ModConfig config;

    public Tooltip(double speed, double jump, int health, Integer strength) {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        
        // Coloring
        int jumpColor = normalColor;
        int speedColor = normalColor;
        int hearthColor = normalColor;
        if(config == null || config.useColors()) {
            if(jump > 4) {jumpColor = goodColor;}
            else if (jump < 2.5) {jumpColor = badColor;};
    
            if(speed > 11) {speedColor = goodColor;} 
            else if (speed < 7) {speedColor = badColor;};
    
            if(health> 25) {hearthColor = goodColor;} 
            else if (health < 20) {hearthColor = badColor;};
        }

        WBox speedBox = new WBox(Axis.HORIZONTAL);
        
        WLabel speedSymbol = new WLabel(new LiteralText("➟"), speedColor);
        WLabel speedLabel = new WLabel(new LiteralText("" + speed), speedColor);
        // WLabel speedRange = new WLabel(new LiteralText("(4.8-14.5)"), normalColor);
        
        // speedSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        speedBox.add(speedSymbol);
        speedBox.add(speedLabel);
        // speedBox.add(speedRange);

        WBox jumpBox = new WBox(Axis.HORIZONTAL);
        
        WLabel jumpSymbol = new WLabel(new LiteralText("⇮"), jumpColor);
        WLabel jumpLabel = new WLabel(new LiteralText("" + jump), jumpColor);
        // WLabel jumpRange = new WLabel(new LiteralText("(1-5.1)"), normalColor);
        
        // jumpSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        jumpBox.add(jumpSymbol);
        jumpBox.add(jumpLabel);
        // jumpBox.add(jumpRange);

        WBox healthBox = new WBox(Axis.HORIZONTAL);
        
        WLabel healthSymbol = new WLabel(new LiteralText("♥"), hearthColor);
        WLabel healthLabel = new WLabel(new LiteralText("" + health), hearthColor);
        // WLabel healthRange = new WLabel(new LiteralText("(15-30)"), normalColor);
        
        // healthSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        healthBox.add(healthSymbol);
        healthBox.add(healthLabel);
        // healthBox.add(healthRange);

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);

        if(strength != null) {
            int strengthColor = normalColor;
            if(config == null || config.useColors()) {
                if(strength > 9) {strengthColor = goodColor;} 
                else if (strength < 6) {strengthColor = badColor;};
            }

            WBox strengthBox = new WBox(Axis.HORIZONTAL);
            WLabel strengthSymbol = new WLabel(new LiteralText("▦"), strengthColor);
            WLabel strengthLabel = new WLabel(new LiteralText("" + strength), strengthColor);
            strengthBox.add(strengthSymbol);
            strengthBox.add(strengthLabel);

            root.add(strengthBox);
        }

        root.validate(this);

    }
}