package net.countered.terrainslabs.mixin.offset.render.target;

import net.countered.terrainslabs.block.interfaces.IOffsetState;
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
import org.spongepowered.asm.mixin.Unique;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockGetter.class)
public interface MixinRaytrace {

    // SPECIAL NOTE: Forge will not accept mixin overwrite if @Unique static methods are used.

    /**
     * Raytrace fix for on offset blocks
     * @author Countered
     * @author Ada Aster
     * @reason Forge doesn't like lambda mixins :(
     */
    @Overwrite
    default BlockHitResult clip(ClipContext context) {
        return BlockGetter.traverseBlocks(
                context.getFrom(), context.getTo(), context,
                (clipContext, blockPos) -> {
                    List<Pair<BlockHitResult, Double>> hits = new ArrayList<>();
                    terrain_slabs$vanillaClip( clipContext, blockPos, hits );

                    terrain_slabs$offsetClip( clipContext, blockPos, Direction.UP, hits );
                    terrain_slabs$offsetClip( clipContext, blockPos, Direction.DOWN, hits );

                    hits.sort( (o1, o2) -> {
                        double diff = o1.getB() - o2.getB();
                        return (int) ( diff > 0 ? Math.ceil(diff) : Math.floor(diff) );
                    });

                    return hits.get( 0 ).getA();
                },
                this::terrain_slabs$vanillaMiss
        );
    }

    @Unique
    default void terrain_slabs$offsetClip(
            ClipContext clipContext, BlockPos pos, Direction dir, List<Pair<BlockHitResult, Double>> list
    ) {
        pos = pos.relative( dir );
        BlockState state = this.getBlockState( pos );
        if ( dir == Direction.UP && !((IOffsetState) state).terrain_slabs$isOffsetAbove() ) {
            return;
        } else if ( dir == Direction.DOWN && !((IOffsetState) state).terrain_slabs$isOffsetBelow() ) {
            return;
        }

        Vec3 from = clipContext.getFrom();
        Vec3 to = clipContext.getTo();
        VoxelShape voxelShape3 = clipContext.getBlockShape(state, (BlockGetter) this, pos);
        BlockHitResult blockHitResult = this.clipWithInteractionOverride(from, to, pos, voxelShape3, state);
        if ( blockHitResult == null ) {
            return;
        }

        double dist = from.distanceToSqr(blockHitResult.getLocation());
        list.add( new Pair<>( blockHitResult, dist ) );
    }

    @Unique
    default void terrain_slabs$vanillaClip(
            ClipContext clipContext, BlockPos pos, List<Pair<BlockHitResult, Double>> list
    ) {
        BlockState blockState = this.getBlockState(pos);
        FluidState fluidState = this.getFluidState(pos);
        Vec3 from = clipContext.getFrom();
        Vec3 to = clipContext.getTo();

        VoxelShape voxelShape = clipContext.getBlockShape(blockState, (BlockGetter) this, pos);
        BlockHitResult blockHitResult = this.clipWithInteractionOverride(from, to, pos, voxelShape, blockState);
        double dist1 = blockHitResult  == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult.getLocation());
        list.add( new Pair<>( blockHitResult, dist1 ) );

        VoxelShape voxelShape2 = clipContext.getFluidShape(fluidState, (BlockGetter) this, pos);
        BlockHitResult blockHitResult2 = voxelShape2.clip(from, to, pos);
        double dist2 = blockHitResult2 == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult2.getLocation());
        list.add( new Pair<>( blockHitResult2, dist2 ) );
    }

    @Unique
    default BlockHitResult terrain_slabs$vanillaMiss( ClipContext ctx ) {
        Vec3 vec3 = ctx.getFrom().subtract(ctx.getTo());
        return BlockHitResult.miss(
                ctx.getTo(),
                Direction.getNearest(vec3.x, vec3.y, vec3.z),
                BlockPos.containing(ctx.getTo())
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