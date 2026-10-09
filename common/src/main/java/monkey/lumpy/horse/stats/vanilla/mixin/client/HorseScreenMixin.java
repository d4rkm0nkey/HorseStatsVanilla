package monkey.lumpy.horse.stats.vanilla.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Mixin(HorseInventoryScreen.class)
public abstract class HorseScreenMixin extends AbstractContainerScreen<HorseInventoryMenu> {
    @Shadow
    @Final
    private AbstractHorse entity;

    private ModConfig config;

    public HorseScreenMixin(HorseInventoryMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor drawContext, int mouseX, int mouseY) {
        super.extractLabels(drawContext, mouseX, mouseY);
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        if (config.showValue()) {
            boolean hasChest = AbstractChestedHorse.class.isAssignableFrom(this.entity.getClass()) && ((AbstractChestedHorse) this.entity).hasChest();
            DecimalFormat df = new DecimalFormat("#.#");
            String jumpStrength = df.format(Converter.jumpStrengthToJumpHeight(this.entity.getAttributeValue(Attributes.JUMP_STRENGTH)));
            String maxHealth = df.format(this.entity.getMaxHealth());
            String speed = df.format(Converter.genericSpeedToBlocPerSec(this.entity.getAttributes().getValue(Attributes.MOVEMENT_SPEED)));

            // Coloring
            Color jumpColor = config.getNeutralColor();
            Color speedColor = config.getNeutralColor();
            Color hearthColor = config.getNeutralColor();
            if(config.useColors()) {
                double jumpValue = new BigDecimal(jumpStrength.replace(',', '.')).doubleValue();
                double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
                double healthValue = new BigDecimal(maxHealth.replace(',', '.')).doubleValue();

                if(jumpValue > config.getGoodHorseJumpValue()) {jumpColor = config.getGoodColor();}
                else if (jumpValue < config.getBadHorseJumpValue()) {jumpColor = config.getBadColor();}

                if(speedValue > config.getGoodHorseSpeedValue()) {speedColor = config.getGoodColor();}
                else if (speedValue < config.getBadHorseSpeedValue()) {speedColor = config.getBadColor();}

                if(healthValue > config.getGoodHorseHeartsValue()) {hearthColor = config.getGoodColor();}
                else if (healthValue < config.getBadHorseHeartsValue()) {hearthColor = config.getBadColor();}
            }

            if (config.valueUp()) {
                drawContext.text(font, "➟ " + speed, 87, 6, speedColor.hashCode(), false);
                drawContext.text(font, "⇮ " + jumpStrength, 122, 6, jumpColor.hashCode(), false);
                drawContext.text(font, "♥ " + maxHealth, 147, 6, hearthColor.hashCode(), false);
                if (config.showMaxMin()) {
                    drawContext.text(font, "➟ (4.7-14.2)", 180, 30, config.getNeutralColor().hashCode(), false);
                    drawContext.text(font, "⇮ (1-5.3)", 180, 40, config.getNeutralColor().hashCode(), false);
                    drawContext.text(font, "♥ (15-30)", 180, 50, config.getNeutralColor().hashCode(), false);
                }
            } else if (!hasChest) {
                if (config.showMaxMin()) {
                    drawContext.text(font, "(4.7-14.2)", 119, 26, config.getNeutralColor().hashCode(), false);
                    drawContext.text(font, "(1-5.3)", 119, 36, config.getNeutralColor().hashCode(), false);
                    drawContext.text(font, "(15-30)", 119, 46, config.getNeutralColor().hashCode(), false);
                }
                drawContext.text(font,  "➟", 82, 26, speedColor.hashCode(), false);
                drawContext.text(font,  speed, 93, 26, speedColor.hashCode(), false);
                drawContext.text(font,  "⇮", 84, 36, jumpColor.hashCode(), false);
                drawContext.text(font, jumpStrength, 93, 36, jumpColor.hashCode(), false);
                drawContext.text(font,  "♥", 83, 46, hearthColor.hashCode(), false);
                drawContext.text(font, maxHealth, 93, 46, hearthColor.hashCode(), false);
            } else {
                drawContext.text(font, "➟ " + speed, 80, 6, speedColor.hashCode(), false);
                drawContext.text(font, "⇮ " + jumpStrength, 115, 6, jumpColor.hashCode(), false);
                drawContext.text(font, "♥ " + maxHealth, 140, 6, hearthColor.hashCode(), false);
            }

            Color strengthColor = config.getNeutralColor();

            if (Llama.class.isAssignableFrom(this.entity.getClass())) {
                int strength = 3 * ((Llama) this.entity).getStrength();

                if(config.useColors()) {
                    if(strength > config.getGoodHorseJumpValue()) {strengthColor = config.getGoodColor();}
                    else if (strength < config.getBadHorseJumpValue()) {strengthColor = config.getBadColor();}
                }
                if (!hasChest) {
                    if (config.valueUp()) {
                        drawContext.text(font, "▦ " + strength, 62, 6, strengthColor.hashCode(), false);
                    } else {
                        drawContext.text(font, "▦", 83, 56, strengthColor.hashCode(), false);
                        drawContext.text(font, String.valueOf(strength), 93, 56, strengthColor.hashCode(), false);
                    }
                }
            }
        }
    }
}
