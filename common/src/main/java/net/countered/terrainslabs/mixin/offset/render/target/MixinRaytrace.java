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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

@Mixin( BlockGetter.class )
public interface MixinRaytrace {

    /**
     * Raytrace fix for on top blocks
     *
     * @author Countered, Ada Aster
     * @reason Architectury forge doesn't like lambda mixins :(
     */
    @Overwrite
    default BlockHitResult clip(ClipContext context) {
        return BlockGetter.traverseBlocks(
                context.getFrom(), context.getTo(), context,
                (clipContext, pos) -> {
                    BlockGetter level = (BlockGetter) (Object) this;
                    List<Pair<BlockHitResult, Double>> hits = new ArrayList<>();
                    terrain_slabs$vanillaClip( level, clipContext, pos, hits );

                    terrain_slabs$offsetClip( level, clipContext, pos.above(), hits );
                    terrain_slabs$offsetClip( level, clipContext, pos.below(), hits );

                    hits.sort( (o1, o2) -> {
                        double diff = o1.getB() - o2.getB();
                        return (int) ( diff > 0 ? Math.ceil(diff) : Math.floor(diff) );
                    });

                    return hits.get( 0 ).getA();
                },
                MixinRaytrace::terrain_slabs$vanillaMiss
        );
    }

    @Unique
    private static void terrain_slabs$offsetClip(
            BlockGetter level, ClipContext clipContext, BlockPos pos, List<Pair<BlockHitResult, Double>> list
    ) {
        BlockState state = level.getBlockState(pos);
        if ( !((IOffsetState) state).terrain_slabs$hasOffsetState() ) {
            return;
        }

        Vec3 from = clipContext.getFrom();
        Vec3 to = clipContext.getTo();
        VoxelShape voxelShape3 = clipContext.getBlockShape(state, level, pos);
        BlockHitResult blockHitResult = level.clipWithInteractionOverride(from, to, pos, voxelShape3, state);
        double dist = blockHitResult == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult.getLocation());

        list.add( new Pair<>( blockHitResult, dist ) );
    }

    @Unique
    private static BlockHitResult terrain_slabs$vanillaMiss( ClipContext ctx ) {
        Vec3 vec3 = ctx.getFrom().subtract(ctx.getTo());
        return BlockHitResult.miss(
                ctx.getTo(),
                Direction.getNearest(vec3.x, vec3.y, vec3.z),
                BlockPos.containing(ctx.getTo())
        );
    }

    @Unique
    private static void terrain_slabs$vanillaClip(
            BlockGetter level, ClipContext clipContext, BlockPos pos, List<Pair<BlockHitResult, Double>> list
    ) {
        BlockState blockState = level.getBlockState(pos);
        FluidState fluidState = level.getFluidState(pos);
        Vec3 from = clipContext.getFrom();
        Vec3 to = clipContext.getTo();

        VoxelShape voxelShape = clipContext.getBlockShape(blockState, level, pos);
        BlockHitResult blockHitResult = level.clipWithInteractionOverride(from, to, pos, voxelShape, blockState);
        double dist1 = blockHitResult  == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult.getLocation());
        list.add( new Pair<>( blockHitResult, dist1 ) );

        VoxelShape voxelShape2 = clipContext.getFluidShape(fluidState, level, pos);
        BlockHitResult blockHitResult2 = voxelShape2.clip(from, to, pos);
        double dist2 = blockHitResult2 == null ? Double.MAX_VALUE : from.distanceToSqr(blockHitResult2.getLocation());
        list.add( new Pair<>( blockHitResult2, dist2 ) );
    }

    @Shadow
    @NotNull
    BlockState getBlockState(BlockPos pos);
}