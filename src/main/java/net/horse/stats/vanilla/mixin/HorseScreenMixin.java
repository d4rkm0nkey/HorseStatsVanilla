package net.horse.stats.vanilla.mixin;

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

import java.text.DecimalFormat;

@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    @Shadow
    @Final
    private HorseBaseEntity entity;

    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);

        boolean hasChest = false;
        if (AbstractDonkeyEntity.class.isAssignableFrom(this.entity.getClass()) && ((AbstractDonkeyEntity) this.entity).hasChest()) {
            hasChest = true;
        }

        DecimalFormat df = new DecimalFormat("#.#");
        String jumpstrength = df.format(this.entity.getJumpStrength() * 10);
        String maxHealth = df.format(this.entity.getMaxHealth());
        String speed = df.format(this.entity.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 100);

        if (!hasChest) {
            this.textRenderer.draw(matrices, "➟ ", 89.0F, 26.0F, 4210752);
            this.textRenderer.draw(matrices, "" + speed, 100.0F, 26.0F, 4210752);
            this.textRenderer.draw(matrices, "⇮", 91.0F, 36.0F, 4210752);
            this.textRenderer.draw(matrices, "" + jumpstrength, 100.0F, 36.0F, 4210752);
            this.textRenderer.draw(matrices, "♥", 90.0F, 46.0F, 4210752);
            this.textRenderer.draw(matrices, "" + maxHealth, 100.0F, 46.0F, 4210752);
        } else {
            this.textRenderer.draw(matrices, "➟ " + speed, 80.0F, 6.0F, 4210752);
            this.textRenderer.draw(matrices, "⇮ " + jumpstrength, 115.0F, 6.0F, 4210752);
            this.textRenderer.draw(matrices, "♥ " + maxHealth, 140.0F, 6.0F, 4210752);
        }

        if (LlamaEntity.class.isAssignableFrom(this.entity.getClass())) {
            int strength = 3 * ((LlamaEntity) this.entity).getStrength();
            if (!hasChest) {
                this.textRenderer.draw(matrices, "▦", 91.0F, 56.0F, 4210752);
                this.textRenderer.draw(matrices, "" + strength, 100.0F, 56.0F, 4210752);
            }
        }
    }
}
