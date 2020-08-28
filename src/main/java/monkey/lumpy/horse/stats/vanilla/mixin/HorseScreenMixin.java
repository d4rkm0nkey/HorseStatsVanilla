package monkey.lumpy.horse.stats.vanilla.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Shadow
    @Final
    private HorseBaseEntity entity;
    private final int normalColor = 4210752;
    private final int badColor = 16733525;
    private final int goodColor = 43520;
    private ModConfig config;

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            System.out.println(config == null);
        }
        boolean hasChest = false;
        if (AbstractDonkeyEntity.class.isAssignableFrom(this.entity.getClass()) && ((AbstractDonkeyEntity) this.entity).hasChest()) {
            hasChest = true;
        }
        DecimalFormat df = new DecimalFormat("#.#");
        String jumpstrength = df.format( 0.695 * (this.entity.getJumpStrength() * 10) - 1.736);
        String maxHealth = df.format(this.entity.getMaxHealth());
        String speed = df.format( 0.42466 * (this.entity.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 100) + 0.112665);

        // Coloring
        int jumpColor = normalColor;
        int speedColor = normalColor;
        int hearthColor = normalColor;
        if(config.useColors()) {
            double jumpValue = new BigDecimal(jumpstrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            int healthValue = new BigDecimal(maxHealth).intValue();
    
            if(jumpValue > 4) {jumpColor = goodColor;}
            else if (jumpValue < 2.5) {jumpColor = badColor;};
    
            if(speedValue > 11) {speedColor = goodColor;} 
            else if (speedValue < 7) {speedColor = badColor;};
    
            if(healthValue > 25) {hearthColor = goodColor;} 
            else if (healthValue < 20) {hearthColor = badColor;};
        }


        if (!hasChest) {
            float spacer = 1.0F;
            if(config.showMaxMin()) {
                this.textRenderer.draw(matrices, "(4.8-14.5)", 119.0F, 26.0F, normalColor);
                this.textRenderer.draw(matrices, "(1-5.1)", 119.0F, 36.0F, normalColor);
                this.textRenderer.draw(matrices, "(15-30)", 119.0F, 46.0F, normalColor);
            } else {
                spacer = 10.0F;
            }
            this.textRenderer.draw(matrices, "➟", 82.0F + spacer, 26.0F, speedColor);
            this.textRenderer.draw(matrices, "" + speed, 93.0F + spacer, 26.0F, speedColor);
            
            this.textRenderer.draw(matrices, "⇮", 84.0F + spacer, 36.0F, jumpColor);
            this.textRenderer.draw(matrices, "" + jumpstrength, 93.0F + spacer, 36.0F, jumpColor);
            this.textRenderer.draw(matrices, "♥", 83.0F + spacer, 46.0F, hearthColor);
            this.textRenderer.draw(matrices, "" + maxHealth, 93.0F + spacer, 46.0F, hearthColor);

        } else {
            this.textRenderer.draw(matrices, "➟ " + speed, 80.0F, 6.0F, speedColor);
            this.textRenderer.draw(matrices, "⇮ " + jumpstrength, 115.0F, 6.0F, jumpColor);
            this.textRenderer.draw(matrices, "♥️ " + maxHealth, 140.0F, 6.0F, hearthColor);
        }

        int strengthColor = normalColor;

        if (LlamaEntity.class.isAssignableFrom(this.entity.getClass())) {
            int strength = 3 * ((LlamaEntity) this.entity).getStrength();

            if(config.useColors()) {
                if(new BigDecimal(strength).doubleValue() > 9) {strengthColor = goodColor;} 
                else if (new BigDecimal(strength).doubleValue() < 6) {strengthColor = badColor;};
            }
            if (!hasChest) {
                this.textRenderer.draw(matrices, "▦", 91.0F, 56.0F, strengthColor);
                this.textRenderer.draw(matrices, "" + strength, 100.0F, 56.0F, strengthColor);
            }
        }
    }
}