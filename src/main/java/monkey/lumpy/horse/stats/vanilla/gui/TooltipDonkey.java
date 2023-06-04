package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.text.Text;

public class TooltipDonkey extends LightweightGuiDescription {
    private ModConfig config;

    public TooltipDonkey(double speed, double jump, int health, int strength) {
        super();
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5,5,0,5));
        
        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color hearthColor = config.getNeutralColor();
        Color strengthColor = config.getNeutralColor();
        if(config == null || config.useColors()) {
            assert config != null;
            if(jump > config.getGoodHorseJumpValue()) {jumpColor = config.getGoodColor();}
            else if (jump < config.getBadHorseJumpValue()) {jumpColor = config.getBadColor();}
    
            if(speed > config.getGoodHorseSpeedValue()) {speedColor = config.getGoodColor();} 
            else if (speed < config.getBadHorseSpeedValue()) {speedColor = config.getBadColor();}
    
            if(health> config.getGoodHorseHeartsValue()) {hearthColor = config.getGoodColor();} 
            else if (health < config.getBadHorseHeartsValue()) {hearthColor = config.getBadColor();}

            if(strength> config.getGoodStrengthValue()) {strengthColor = config.getGoodColor();}
            else if (strength < config.getBadStrengthValue()) {strengthColor = config.getBadColor();}
        }

        WBox speedBox = new WBox(Axis.HORIZONTAL);
        
        WLabel speedSymbol = new WLabel(Text.literal("➟"), speedColor.hashCode());
        WLabel speedLabel = new WLabel(Text.literal(String.valueOf(speed)), speedColor.hashCode());

        speedBox.add(speedSymbol);
        speedBox.add(speedLabel);

        WBox jumpBox = new WBox(Axis.HORIZONTAL);
        
        WLabel jumpSymbol = new WLabel(Text.literal("⇮"), jumpColor.hashCode());
        WLabel jumpLabel = new WLabel(Text.literal(String.valueOf(jump)), jumpColor.hashCode());

        jumpBox.add(jumpSymbol);
        jumpBox.add(jumpLabel);

        WBox healthBox = new WBox(Axis.HORIZONTAL);
        
        WLabel healthSymbol = new WLabel(Text.literal("♥"), hearthColor.hashCode());
        WLabel healthLabel = new WLabel(Text.literal(String.valueOf(health)), hearthColor.hashCode());

        healthBox.add(healthSymbol);
        healthBox.add(healthLabel);

        WBox strengthBox = new WBox(Axis.HORIZONTAL);

        WLabel strengthSymbol = new WLabel(Text.literal("▦"), strengthColor.hashCode());
        WLabel strengthLabel = new WLabel(Text.literal(String.valueOf(strength)), strengthColor.hashCode());

        strengthBox.add(strengthSymbol);
        strengthBox.add(strengthLabel);

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);
        root.add(strengthBox);
        root.validate(this);
    }
}
