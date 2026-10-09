package monkey.lumpy.horse.stats.vanilla.mixin.client;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.shedaniel.autoconfig.AutoConfig;
import monkey.lumpy.horse.stats.vanilla.config.ModConfig;
import monkey.lumpy.horse.stats.vanilla.gui.ToolTipGui;
import monkey.lumpy.horse.stats.vanilla.gui.TooltipDonkey;
import monkey.lumpy.horse.stats.vanilla.util.Converter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractDonkeyEntityMixin extends AbstractHorse {
    @Shadow public abstract int getInventoryColumns();

    private ModConfig config;

    protected AbstractDonkeyEntityMixin(EntityType<? extends AbstractHorse> entityType, Level world) {
        super(entityType, world);
    }


    @Inject(at = @At("HEAD"), method = "mobInteract")
    public InteractionResult interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ret) {
        // In singleplayer interactMob also runs on the integrated server thread,
        // where opening a screen crashes the client
        if (!this.level().isClientSide()) {
            return ret.getReturnValue();
        }

        if(config == null) {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }

        if (config.showValue() && !this.isTamed() && player.isSecondaryUseActive() && (config == null || config.isTooltipEnabled())) {
            // Show tooltip
            DecimalFormat df = new DecimalFormat("#.#");
            String jumpStrength = df.format( Converter.jumpStrengthToJumpHeight(this.getAttributeValue(Attributes.JUMP_STRENGTH)) );
            String maxHealth = df.format(this.getMaxHealth());
            String strength = df.format(3L * this.getInventoryColumns());
            String speed = df.format(Converter.genericSpeedToBlocPerSec(this.getAttributes().getValue(Attributes.MOVEMENT_SPEED)));
            
            double jumpValue = new BigDecimal(jumpStrength.replace(',', '.')).doubleValue();
            double speedValue = new BigDecimal(speed.replace(',', '.')).doubleValue();
            int healthValue = new BigDecimal(maxHealth.replace(',', '.')).intValue();
            int strengthValue = new BigDecimal(strength.replace(',', '.')).intValue();

            Minecraft.getInstance().setScreen(
                new ToolTipGui(new TooltipDonkey(speedValue, jumpValue, healthValue, strengthValue))
            );
        }
        return ret.getReturnValue();
    }
}
