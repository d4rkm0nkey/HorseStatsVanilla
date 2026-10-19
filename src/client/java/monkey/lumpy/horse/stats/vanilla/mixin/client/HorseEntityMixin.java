package monkey.lumpy.horse.stats.vanilla.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin {
    @Inject(at = @At("HEAD"), method = "interactMob")
    private void showStats(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        ToolTipGui.openOnInteract((AbstractHorseEntity) (Object) this, player);
    }
}
