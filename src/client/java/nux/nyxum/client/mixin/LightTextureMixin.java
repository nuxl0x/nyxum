package nux.nyxum.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import nux.nyxum.client.power.ClientFlagCache;
import nux.nyxum.power.PlayerFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

	@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private static void onGetBrightness(DimensionType dimensionType, int lightLevel, CallbackInfoReturnable<Float> cir) {
		if (ClientFlagCache.INSTANCE.getFlag(PlayerFlag.HAS_NIGHT_VISION)) {
			cir.setReturnValue(1.0F);
		}
	}
}