package info.u_team.overworld_mirror.event;

import info.u_team.overworld_mirror.portal.PortalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.phys.Vec3;

public class PortalCreationEventHandler {
	
	public static void onBonemeal(BlockPos pos, ServerLevel level, ServerPlayer player) {
		if (!(level.getBlockState(pos).getBlock() instanceof FlowerBlock) || !player.isShiftKeyDown()) {
			return;
		}
		
		if (PortalManager.trySpawnPortalFromFrame(level, pos)) {
			final LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
			lightning.moveTo(Vec3.atBottomCenterOf(pos.above()));
			lightning.setVisualOnly(true);
			
			lightning.setCause(player);
			
			level.addFreshEntity(lightning);
		}
	}
}
