package monkey.lumpy.horse.stats.vanilla.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;

import java.text.DecimalFormat;

@Mixin(HorseInventoryScreen.class)
public abstract class HorseScreenMixin extends AbstractMountInventoryScreen<HorseInventoryMenu> {

    public HorseScreenMixin(HorseInventoryMenu menu, Inventory inventory, Component title,
                            int inventoryColumns, LivingEntity mount) {
        super(menu, inventory, title, inventoryColumns, mount);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractLabels(guiGraphics, mouseX, mouseY);
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (config.showValue()) {
            AbstractHorse horse = (AbstractHorse) this.mount;
            boolean hasChest = horse instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest();

            double jumpValue = Math.round(Converter.jumpStrengthToJumpHeight(horse.getAttributeValue(Attributes.JUMP_STRENGTH)) * 10.0) / 10.0;
            double speedValue = Math.round(Converter.genericSpeedToBlocPerSec(horse.getAttributeValue(Attributes.MOVEMENT_SPEED)) * 10.0) / 10.0;
            double healthValue = Math.round(horse.getMaxHealth() * 10.0) / 10.0;

            DecimalFormat df = new DecimalFormat("#.#");
            String jumpStrength = df.format(jumpValue);
            String maxHealth = df.format(healthValue);
            String speed = df.format(speedValue);

            Color jumpColor = config.getNeutralColor();
            Color speedColor = config.getNeutralColor();
            Color healthColor = config.getNeutralColor();

            if (config.useColors()) {
                if (jumpValue > config.getGoodHorseJumpValue()) jumpColor = config.getGoodColor();
                else if (jumpValue < config.getBadHorseJumpValue()) jumpColor = config.getBadColor();

                if (speedValue > config.getGoodHorseSpeedValue()) speedColor = config.getGoodColor();
                else if (speedValue < config.getBadHorseSpeedValue()) speedColor = config.getBadColor();

                if (healthValue > config.getGoodHorseHeartsValue()) healthColor = config.getGoodColor();
                else if (healthValue < config.getBadHorseHeartsValue()) healthColor = config.getBadColor();
            }

            if (config.valueUp()) {
                guiGraphics.text(this.font, "➟ " + speed, 87, 6, speedColor.hashCode(), false);
                guiGraphics.text(this.font, "⇮ " + jumpStrength, 122, 6, jumpColor.hashCode(), false);
                guiGraphics.text(this.font, "♥ " + maxHealth, 147, 6, healthColor.hashCode(), false);

                if (config.showMaxMin()) {
                    guiGraphics.text(this.font, "➟ (4.7-14.2)", 180, 30, config.getNeutralColor().hashCode(), false);
                    guiGraphics.text(this.font, "⇮ (1-5.3)", 180, 40, config.getNeutralColor().hashCode(), false);
                    guiGraphics.text(this.font, "♥ (15-30)", 180, 50, config.getNeutralColor().hashCode(), false);
                }
            } else if (!hasChest) {
                if (config.showMaxMin()) {
                    guiGraphics.text(this.font, "(4.7-14.2)", 119, 26, config.getNeutralColor().hashCode(), false);
                    guiGraphics.text(this.font, "(1-5.3)", 119, 36, config.getNeutralColor().hashCode(), false);
                    guiGraphics.text(this.font, "(15-30)", 119, 46, config.getNeutralColor().hashCode(), false);
                }
                guiGraphics.text(this.font, "➟", 82, 26, speedColor.hashCode(), false);
                guiGraphics.text(this.font, speed, 93, 26, speedColor.hashCode(), false);
                guiGraphics.text(this.font, "⇮", 84, 36, jumpColor.hashCode(), false);
                guiGraphics.text(this.font, jumpStrength, 93, 36, jumpColor.hashCode(), false);
                guiGraphics.text(this.font, "♥", 83, 46, healthColor.hashCode(), false);
                guiGraphics.text(this.font, maxHealth, 93, 46, healthColor.hashCode(), false);
            } else {
                guiGraphics.text(this.font, "➟ " + speed, 80, 6, speedColor.hashCode(), false);
                guiGraphics.text(this.font, "⇮ " + jumpStrength, 115, 6, jumpColor.hashCode(), false);
                guiGraphics.text(this.font, "♥ " + maxHealth, 140, 6, healthColor.hashCode(), false);
            }

            if (horse instanceof Llama llama) {
                int strength = 3 * llama.getStrength();
                Color strengthColor = config.getNeutralColor();

                if (config.useColors()) {
                    if (strength > config.getGoodHorseJumpValue()) strengthColor = config.getGoodColor();
                    else if (strength < config.getBadHorseJumpValue()) strengthColor = config.getBadColor();
                }

                if (!hasChest) {
                    if (config.valueUp()) {
                        guiGraphics.text(this.font, "▦ " + strength, 62, 6, strengthColor.hashCode(), false);
                    } else {
                        guiGraphics.text(this.font, "▦", 83, 56, strengthColor.hashCode(), false);
                        guiGraphics.text(this.font, String.valueOf(strength), 93, 56, strengthColor.hashCode(), false);
                    }
                }
            }
        }
    }
}
