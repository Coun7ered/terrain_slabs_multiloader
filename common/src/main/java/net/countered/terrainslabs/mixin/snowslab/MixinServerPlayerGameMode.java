package net.countered.terrainslabs.mixin.snowslab;

import net.countered.terrainslabs.snowslab.SnowSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Region-aware left-click breaking of a SnowSlabBlock:
 *   - Looking at the SNOW portion (upper part) -> strip snow only, keep the bare slab.
 *   - Looking at the BASE SLAB portion (lower part) -> break the whole block normally.
 *
 * We recompute where the player is aiming on the block (a short clip along their view
 * ray) and compare the hit Y to the slab/snow split (y=8 => 0.5 within the block).
 */
@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode {

    @Shadow
    public net.minecraft.server.level.ServerLevel level;

    @Shadow
    public net.minecraft.server.level.ServerPlayer player;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void clearSnowSlabInsteadOfBreaking(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Level level = this.level;
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SnowSlabBlock)) return;

        // Only clear-snow when the player is aiming at the SNOW region. If they're
        // aiming at the base slab, fall through and let the whole block break.
        if (SnowSlabBlock.isAimingAtSnowRegion(level, pos, state, this.player)) {
            if (SnowSlabBlock.clearSnowOnBreak(level, pos, state, this.player)) {
                cir.setReturnValue(false); // snow cleared, slab kept: don't destroy
            }
        }
        // else: aiming at the base slab -> do nothing here -> normal break proceeds.
    }
}
