package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.text.LiteralText;

public class Tooltip extends LightweightGuiDescription {
    private ModConfig config;

    public Tooltip(double speed, double jump, double health) {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        
        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color hearthColor = config.getNeutralColor();
        if(config == null || config.useColors()) {
            if(jump > config.getGoodHorseJumpValue()) {jumpColor = config.getGoodColor();;}
            else if (jump < config.getBadHorseJumpValue()) {jumpColor = config.getBadColor();};
    
            if(speed > config.getGoodHorseSpeedValue()) {speedColor = config.getGoodColor();} 
            else if (speed < config.getBadHorseSpeedValue()) {speedColor = config.getBadColor();};
    
            if(health> config.getGoodHorseHeartsValue()) {hearthColor = config.getGoodColor();} 
            else if (health < config.getBadHorseHeartsValue()) {hearthColor = config.getBadColor();};
        }

        WBox speedBox = new WBox(Axis.HORIZONTAL);
        
        WLabel speedSymbol = new WLabel(new LiteralText("➟"), speedColor.hashCode());
        WLabel speedLabel = new WLabel(new LiteralText("" + speed), speedColor.hashCode());
        // WLabel speedRange = new WLabel(new LiteralText("(4.8-14.5)"), normalColor);
        
        // speedSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        speedBox.add(speedSymbol);
        speedBox.add(speedLabel);
        // speedBox.add(speedRange);

        WBox jumpBox = new WBox(Axis.HORIZONTAL);
        
        WLabel jumpSymbol = new WLabel(new LiteralText("⇮"), jumpColor.hashCode());
        WLabel jumpLabel = new WLabel(new LiteralText("" + jump), jumpColor.hashCode());
        // WLabel jumpRange = new WLabel(new LiteralText("(1-5.1)"), normalColor);
        
        // jumpSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        jumpBox.add(jumpSymbol);
        jumpBox.add(jumpLabel);
        // jumpBox.add(jumpRange);

        WBox healthBox = new WBox(Axis.HORIZONTAL);
        
        WLabel healthSymbol = new WLabel(new LiteralText("♥"), hearthColor.hashCode());
        WLabel healthLabel = new WLabel(new LiteralText("" + health), hearthColor.hashCode());
        // WLabel healthRange = new WLabel(new LiteralText("(15-30)"), normalColor);
        
        // healthSymbol.setVerticalAlignment(VerticalAlignment.CENTER);

        healthBox.add(healthSymbol);
        healthBox.add(healthLabel);
        // healthBox.add(healthRange);

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);
        root.validate(this);

    }
}