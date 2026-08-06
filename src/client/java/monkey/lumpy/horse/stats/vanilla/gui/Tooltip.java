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

public class Tooltip extends LightweightGuiDescription {
    private final ModConfig config;

    public Tooltip(double speed, double jump, int health) {
        super();
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        WBox root = new WBox(Axis.VERTICAL);
        setRootPanel(root);
        root.setSpacing(-8);
        root.setInsets(new Insets(5, 5, 0, 5));

        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color healthColor = config.getNeutralColor();

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

        root.add(speedBox);
        root.add(jumpBox);
        root.add(healthBox);
        root.validate(this);
    }
}
