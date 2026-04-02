package net.countered.terrainslabs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockGetter.class)
public interface BlockGetterMixin {

    /**
     * @author Countered
     * @reason Forge doesn't seem to allow lambda injection :(
     */
    @Overwrite
    default BlockHitResult clip(ClipContext context) {
        return BlockGetter.traverseBlocks(
                context.getFrom(), context.getTo(), context,
                (clipContext, blockPos) -> {
                    BlockState blockState = this.getBlockState(blockPos);
                    FluidState fluidState = this.getFluidState(blockPos);
                    Vec3 from = clipContext.getFrom();
                    Vec3 to = clipContext.getTo();

                    VoxelShape voxelShape = clipContext.getBlockShape(blockState, (BlockGetter) this, blockPos);
                    BlockHitResult blockHitResult = this.clipWithInteractionOverride(from, to, blockPos, voxelShape, blockState);

                    VoxelShape voxelShape2 = clipContext.getFluidShape(fluidState, (BlockGetter) this, blockPos);
                    BlockHitResult blockHitResult2 = voxelShape2.clip(from, to, blockPos);

                    double d = blockHitResult  == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult.getLocation());
                    double e = blockHitResult2 == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult2.getLocation());
                    BlockHitResult vanillaResult = d <= e ? blockHitResult : blockHitResult2;

                    BlockPos abovePos = blockPos.above();
                    BlockState blockStateAbove = this.getBlockState(abovePos);
                    VoxelShape voxelShape3 = clipContext.getBlockShape(blockStateAbove, (BlockGetter) this, abovePos);
                    BlockHitResult blockHitResultAbove = this.clipWithInteractionOverride(from, to, abovePos, voxelShape3, blockStateAbove);

                    double f = blockHitResultAbove == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResultAbove.getLocation());

                    if (f <= d && f <= e) {
                        return blockHitResultAbove;
                    }

                    return vanillaResult;
                },
                clipContext -> {
                    Vec3 vec3 = clipContext.getFrom().subtract(clipContext.getTo());
                    return BlockHitResult.miss(
                            clipContext.getTo(),
                            Direction.getNearest(vec3.x, vec3.y, vec3.z),
                            BlockPos.containing(clipContext.getTo())
                    );
                }
        );
    }

    @Shadow
    BlockState getBlockState(BlockPos pos);

    @Shadow
    FluidState getFluidState(BlockPos pos);

    @Shadow
    @Nullable
    BlockHitResult clipWithInteractionOverride(Vec3 startVec, Vec3 endVec, BlockPos pos, VoxelShape shape, BlockState state);
}