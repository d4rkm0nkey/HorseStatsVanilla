package net.horse.stats.vanilla.mixin;

import java.text.DecimalFormat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.HorseScreenHandler;


@Mixin(HorseScreen.class)
public abstract class HorseScreenMixin extends HandledScreen<HorseScreenHandler> {
    public HorseScreenMixin(HorseScreenHandler handler, PlayerInventory inventory, HorseBaseEntity entity) {
        super(handler, inventory, entity.getDisplayName());
    }

    HorseBaseEntity horseBaseEntity;
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(HorseScreenHandler handler, PlayerInventory playerInventory_1, HorseBaseEntity horseBaseEntity_1, CallbackInfo ci) {
        horseBaseEntity = horseBaseEntity_1;
    }

    @Inject(method = "onDrawForeground", at = @At("RETURN"))
    private void onDrawForeground(int int_1, int int_2, CallbackInfo ci) {
        boolean hasChest = false;
        if(AbstractDonkeyEntity.class.isAssignableFrom(horseBaseEntity.getClass())) {
            if(((AbstractDonkeyEntity) horseBaseEntity).hasChest()) {
                hasChest = true;
            }
        }
        DecimalFormat df = new DecimalFormat("#.#"); 
        String jumpstrength = df.format(horseBaseEntity.getJumpStrength() * 10);
        // String maxHealth = df.format(horseBaseEntity.getMaximumHealth());
        // String speed = df.format(horseBaseEntity.getAttributes().get(EntityAttributes.MOVEMENT_SPEED).getValue() * 100);
        // if(!hasChest) {
        //     this.font.draw("➟ ", 89.0F, 26.0F, 4210752);
        //     this.font.draw("" + speed, 100.0F, 26.0F, 4210752);
        //     this.font.draw("⇮", 91.0F, 36.0F, 4210752);
        //     this.font.draw("" + jumpstrength, 100.0F, 36.0F, 4210752);
        //     this.font.draw("♥", 90.0F, 46.0F, 4210752);
        //     this.font.draw("" + maxHealth, 100.0F, 46.0F, 4210752);
        // } else {
        //     this.font.draw("➟ " + speed, 80.0F, 6.0F, 4210752);
        //     this.font.draw("⇮ " + jumpstrength, 115.0F, 6.0F, 4210752);
        //     this.font.draw("♥ " +  maxHealth, 140.0F, 6.0F, 4210752);
        // }
        // if(LlamaEntity.class.isAssignableFrom(horseBaseEntity.getClass())) {
        //     int strength = 3 * ((LlamaEntity)horseBaseEntity).getStrength();
        //     if(!hasChest) {
        //         this.font.draw("▦", 91.0F, 56.0F, 4210752);
        //         this.font.draw("" + strength, 100.0F, 56.0F, 4210752);
        //     }
        // }
    }
}