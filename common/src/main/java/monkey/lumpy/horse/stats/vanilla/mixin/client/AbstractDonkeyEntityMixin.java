package monkey.lumpy.horse.stats.vanilla.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;

/** Donkeys, mules and llamas. */
@Mixin(AbstractChestedHorse.class)
public abstract class AbstractDonkeyEntityMixin {
    @Inject(at = @At("HEAD"), method = "mobInteract")
    private void showStats(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ret) {
        ToolTipGui.openOnInteract((AbstractHorse) (Object) this, player);
    }
}
