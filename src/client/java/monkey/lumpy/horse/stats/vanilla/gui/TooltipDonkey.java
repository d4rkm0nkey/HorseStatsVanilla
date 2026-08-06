package monkey.lumpy.horse.stats.vanilla.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import net.minecraft.network.chat.Component;

public class TooltipDonkey extends LightweightGuiDescription {
    private final ModConfig config;

    public TooltipDonkey(double speed, double jump, int health, int strength) {
        super();
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5, 5, 0, 5));

        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color healthColor = config.getNeutralColor();
        Color strengthColor = config.getNeutralColor();

        if (config.useColors()) {
            if (jump > config.getGoodHorseJumpValue()) {
                jumpColor = config.getGoodColor();
            } else if (jump < config.getBadHorseJumpValue()) {
                jumpColor = config.getBadColor();
            }

            if (speed > config.getGoodHorseSpeedValue()) {
                speedColor = config.getGoodColor();
            } else if (speed < config.getBadHorseSpeedValue()) {
                speedColor = config.getBadColor();
            }

            if (health > config.getGoodHorseHeartsValue()) {
                healthColor = config.getGoodColor();
            } else if (health < config.getBadHorseHeartsValue()) {
                healthColor = config.getBadColor();
            }

            if (strength > config.getGoodStrengthValue()) {
                strengthColor = config.getGoodColor();
            } else if (strength < config.getBadStrengthValue()) {
                strengthColor = config.getBadColor();
            }
        }

        WBox speedBox = new WBox(Axis.HORIZONTAL);
        speedBox.add(new WLabel(Component.literal("➟"), speedColor.hashCode()));
        speedBox.add(new WLabel(Component.literal(String.valueOf(speed)), speedColor.hashCode()));

        WBox jumpBox = new WBox(Axis.HORIZONTAL);
        jumpBox.add(new WLabel(Component.literal("⇮"), jumpColor.hashCode()));
        jumpBox.add(new WLabel(Component.literal(String.valueOf(jump)), jumpColor.hashCode()));

        WBox healthBox = new WBox(Axis.HORIZONTAL);
        healthBox.add(new WLabel(Component.literal("♥"), healthColor.hashCode()));
        healthBox.add(new WLabel(Component.literal(String.valueOf(health)), healthColor.hashCode()));

        WBox strengthBox = new WBox(Axis.HORIZONTAL);
        strengthBox.add(new WLabel(Component.literal("▦"), strengthColor.hashCode()));
        strengthBox.add(new WLabel(Component.literal(String.valueOf(strength)), strengthColor.hashCode()));

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);
        root.add(strengthBox);
        root.validate(this);
    }
}
