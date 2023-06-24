package info.u_team.overworld_mirror.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import info.u_team.overworld_mirror.event.PortalCreationEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

@Mixin(BoneMealItem.class)
abstract class BoneMealItemMixin {
	
	@Inject(method = "useOn", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BoneMealItem;growCrop(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"), cancellable = true)
	private void overworldmirror$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> result, Level level, BlockPos clickedPos, BlockPos unused) {
		if (level instanceof final ServerLevel serverLevel && context.getPlayer() instanceof ServerPlayer player) {
			PortalCreationEventHandler.onBonemeal(clickedPos, serverLevel, player);
		}
	}
	
}
