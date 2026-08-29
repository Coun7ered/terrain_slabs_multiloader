package net.countered.terrainslabs.mixin.offset.block_tweaks;

import net.countered.terrainslabs.api.IConditionalOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( targets = { "net.minecraft.world.level.block.GrowingPlantBlock" } )
public class MixinGrowingPlantBlock implements IConditionalOffset {
    @Shadow
    @Final
    protected Direction growthDirection;

    @Override
    public <L extends BlockGetter> boolean couldBeOntop(L level, BlockPos pos, BlockState state) {
        return growthDirection == Direction.UP;
    }

    @Override
    public <L extends BlockGetter> boolean couldBeOnbottom(L level, BlockPos pos, BlockState state) {
        return growthDirection == Direction.DOWN;
    }
}
