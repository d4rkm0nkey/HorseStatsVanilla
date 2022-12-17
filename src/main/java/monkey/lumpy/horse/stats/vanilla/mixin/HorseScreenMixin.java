package monkey.lumpy.horse.stats.vanilla.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.util.Converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Shadow
    @Final
    private AbstractHorseEntity entity;
    private ModConfig config;

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        boolean hasChest = false;
        if (AbstractDonkeyEntity.class.isAssignableFrom(this.entity.getClass()) && ((AbstractDonkeyEntity) this.entity).hasChest()) {
            hasChest = true;
        }
        DecimalFormat df = new DecimalFormat("#.#");
        String jumpstrength = df.format(Converter.jumpStrengthToJumpHeight(this.entity.getJumpStrength()));
        String maxHealth = df.format(this.entity.getMaxHealth());
        String speed = df.format(Converter.genericSpeedToBlocPerSec(this.entity.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));

        // Coloring
        Color jumpColor = config.getNeutralColor();
        Color speedColor = config.getNeutralColor();
        Color hearthColor = config.getNeutralColor();
        if(config.useColors()) {
            double jumpValue = new BigDecimal(jumpstrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            double healthValue = new BigDecimal(maxHealth.replace(',', '.')).doubleValue();
    
            if(jumpValue > config.getGoodHorseJumpValue()) {jumpColor = config.getGoodColor();}
            else if (jumpValue < config.getBadHorseJumpValue()) {jumpColor = config.getBadColor();};
    
            if(speedValue > config.getGoodHorseSpeedValue()) {speedColor = config.getGoodColor();} 
            else if (speedValue < config.getBadHorseSpeedValue()) {speedColor = config.getBadColor();};
    
            if(healthValue > config.getGoodHorseHeartsValue()) {hearthColor = config.getGoodColor();} 
            else if (healthValue < config.getBadHorseHeartsValue()) {hearthColor = config.getBadColor();};
        }


        if (!hasChest) {
            float spacer = 1.0F;
            if(config.showMaxMin()) {
                this.textRenderer.draw(matrices, "(3.8-14.2)", 119.0F, 26.0F, config.getNeutralColor().hashCode());
                this.textRenderer.draw(matrices, "(1-5.3)", 119.0F, 36.0F, config.getNeutralColor().hashCode());
                this.textRenderer.draw(matrices, "(15-32)", 119.0F, 46.0F, config.getNeutralColor().hashCode());
            } else {
                spacer = 10.0F;
            }
            this.textRenderer.draw(matrices, "➟", 82.0F + spacer, 26.0F, speedColor.hashCode());
            this.textRenderer.draw(matrices, "" + speed, 93.0F + spacer, 26.0F, speedColor.hashCode());
            
            this.textRenderer.draw(matrices, "⇮", 84.0F + spacer, 36.0F, jumpColor.hashCode());
            this.textRenderer.draw(matrices, "" + jumpstrength, 93.0F + spacer, 36.0F, jumpColor.hashCode());
            this.textRenderer.draw(matrices, "♥", 83.0F + spacer, 46.0F, hearthColor.hashCode());
            this.textRenderer.draw(matrices, "" + maxHealth, 93.0F + spacer, 46.0F, hearthColor.hashCode());

        } else {
            this.textRenderer.draw(matrices, "➟ " + speed, 80.0F, 6.0F, speedColor.hashCode());
            this.textRenderer.draw(matrices, "⇮ " + jumpstrength, 115.0F, 6.0F, jumpColor.hashCode());
            this.textRenderer.draw(matrices, "♥ " + maxHealth, 140.0F, 6.0F, hearthColor.hashCode());
        }

        Color strengthColor = config.getNeutralColor();

        if (LlamaEntity.class.isAssignableFrom(this.entity.getClass())) {
            int strength = 3 * ((LlamaEntity) this.entity).getStrength();

            if(config.useColors()) {
                if(strength > config.getGoodHorseJumpValue()) {strengthColor = config.getGoodColor();} 
                else if (strength < config.getBadHorseJumpValue()) {strengthColor = config.getBadColor();};
            }
            if (!hasChest) {
                this.textRenderer.draw(matrices, "▦", 91.0F, 56.0F, strengthColor.hashCode());
                this.textRenderer.draw(matrices, "" + strength, 100.0F, 56.0F, strengthColor.hashCode());
            }
        }
    }
}
