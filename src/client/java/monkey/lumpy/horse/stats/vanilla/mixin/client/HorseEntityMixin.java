package monkey.lumpy.horse.stats.vanilla.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.Tooltip;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public abstract class HorseEntityMixin extends AbstractHorse {

    protected HorseEntityMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract")
    public InteractionResult mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ret) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (config.showValue() && !this.isTamed() && player.isSecondaryUseActive() && config.isTooltipEnabled()) {

            double jumpRaw = Converter.jumpStrengthToJumpHeight(this.getAttributeValue(Attributes.JUMP_STRENGTH));
            double speedRaw = Converter.genericSpeedToBlocPerSec(this.getAttributeValue(Attributes.MOVEMENT_SPEED));

            double jumpValue = Math.round(jumpRaw * 10.0) / 10.0;
            double speedValue = Math.round(speedRaw * 10.0) / 10.0;
            int healthValue = Math.round(this.getMaxHealth());

            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreenAndShow(
                    new ToolTipGui(new Tooltip(speedValue, jumpValue, healthValue))
            ));
        }
        return ret.getReturnValue();
    }
}
