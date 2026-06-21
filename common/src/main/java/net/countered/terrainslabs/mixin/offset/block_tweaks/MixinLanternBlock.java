package net.countered.terrainslabs.mixin.offset.block_tweaks;

import net.countered.terrainslabs.api.IConditionalOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("DataFlowIssue")
@Mixin( LanternBlock.class )
public class MixinLanternBlock implements IConditionalOffset {

    @Shadow
    @Final
    public static BooleanProperty HANGING;

    @Override
    public <L extends BlockGetter> boolean couldPlaceOntop(L level, BlockPos pos, BlockState state) {
        return !state.getValue( HANGING );
    }

    @Override
    public <L extends BlockGetter> boolean couldPlaceOnbottom(L level, BlockPos pos, BlockState state) {
        return !couldPlaceOntop( level, pos, state );
    }
}
