package monkey.lumpy.horse.stats.vanilla.mixin.client;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.Tooltip;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends AbstractHorseEntity {

    private ModConfig config;

    protected HorseEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(at = @At("HEAD"), method = "interactMob")
    public ActionResult interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        
        if (config.showValue() && !this.isTame() && player.shouldCancelInteraction() && (config == null || config.isTooltipEnabled())) {
            // Show tooltip
            DecimalFormat df = new DecimalFormat("#.#");
            String jumpStrength = df.format( Converter.jumpStrengthToJumpHeight(this.getJumpStrength()) );
            String maxHealth = df.format(this.getMaxHealth());
            String speed = df.format(Converter.genericSpeedToBlocPerSec(this.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
            
            double jumpValue = new BigDecimal(jumpStrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            int healthValue = new BigDecimal(maxHealth.replace(',', '.')).intValue();

            MinecraftClient.getInstance().setScreen(
                new ToolTipGui(new Tooltip(speedValue, jumpValue, healthValue))
            );
        }
        return ret.getReturnValue();
    }
}
