package monkey.lumpy.horse.stats.vanilla.mixin;

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
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends HorseBaseEntity {

    private ModConfig config;

    protected HorseEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(at = @At("HEAD"), method = "interactMob")
    public ActionResult interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        
        if (this.world.isClient && !this.isTame() && player.shouldCancelInteraction() && (config == null || config.isTooltipEnabled())) {
            // Show tooltip
            DecimalFormat df = new DecimalFormat("#.#");
            String jumpstrength = df.format( Converter.jumpStrengthToJumpHeight(this.getJumpStrength()) );
            String maxHealth = df.format(this.getMaxHealth());
            String speed = df.format(Converter.genericSpeedToBlocPerSec(this.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));         
            
            double jumpValue = new BigDecimal(jumpstrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            double healthValue = new BigDecimal(maxHealth.replace(',', '.')).doubleValue();

            System.out.println("Hello!");

            MinecraftClient.getInstance().openScreen(
                new ToolTipGui(new Tooltip(speedValue, jumpValue, healthValue))
            );
        }
        return ret.getReturnValue();
    }
}