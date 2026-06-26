package net.countered.terrainslabs.mixin.offset.state;

import com.google.common.collect.ImmutableMap;
import dev.architectury.platform.Platform;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.mixin_applier.EarlyConfigReader;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Logger;

@Mixin( Block.class )
public abstract class MixinBlock extends BlockBehaviour {

    @Shadow
    public abstract MutableComponent getName();

    @Shadow
    public abstract StateDefinition<Block, BlockState> getStateDefinition();

    @Shadow
    public abstract BlockState defaultBlockState();

    @Unique
    private boolean terrain_slabs$stateFixed = false;

    @Shadow
    public static int getId( BlockState state ) { return -1; }

    @Shadow
    @Final
    protected StateDefinition<Block, BlockState> stateDefinition;

    MixinBlock(Properties properties) {
        super(properties);
    }

    @Inject( method = "getShapeForEachState", at = @At("HEAD") )
    private void terrain_slabs$ensureDefinition(
            Function<BlockState, VoxelShape> shapeGetter, CallbackInfoReturnable<ImmutableMap<BlockState, VoxelShape>> cir
    ) {
        if ( !terrain_slabs$stateFixed ) {
            this.getStateDefinition();
        }
    }

    @Inject( method = "getStateDefinition", at = @At("HEAD"))
    private void terrain_slabs$stateAdder(CallbackInfoReturnable<StateDefinition<Block, BlockState>> cir) {
        if ( terrain_slabs$stateFixed ) return;
        Block thisBlock = (Block) (Object) this;

        boolean includeOntop = EarlyConfigReader.ONTOP_INCLUDE.contains( thisBlock );
        boolean includeOnbottom = EarlyConfigReader.ONBOTTOM_INCLUDE.contains(thisBlock);
        Property<OffsetProperty.OffsetType> newOffset = terrain_slabs$getNewProperty( includeOntop, includeOnbottom );
        if ( newOffset == null ) {
            terrain_slabs$stateFixed = true;
            return;
        }

        try {
            BlockState defaultState = this.defaultBlockState();
            Collection<Property<?>> properties = this.stateDefinition.getProperties();

            StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>( thisBlock );
            this.createBlockStateDefinition(builder);
            builder.add( newOffset );

            terrain_slabs$modifyStateDef( builder.create(Block::defaultBlockState, BlockState::new) );
            this.registerDefaultState( terrain_slabs$transferDefaultState( defaultState, properties, newOffset ) );
        } catch ( Exception ignored ) {
            LoggerFactory.getLogger(TerrainSlabs.MOD_ID ).info( "Failed to update statedef for {}.", this.getName().toString() );
        }

        terrain_slabs$stateFixed = true;
    }

    @Unique
    private void terrain_slabs$modifyStateDef(StateDefinition<Block, BlockState> newDef ) throws NoSuchFieldException, IllegalAccessException {
        Field stateDef = Block.class.getDeclaredField("stateDefinition");
        if ( !Platform.isDevelopmentEnvironment() ) {
            stateDef = Block.class.getDeclaredField("f_49792_"); // Obfuscated Name
        }

        stateDef.setAccessible(true);
        stateDef.set(this, newDef);
    }

    @Unique
    private BlockState terrain_slabs$transferDefaultState( BlockState oldState, Collection<Property<?>> properties, Property<OffsetProperty.OffsetType> offsetProperty ) {
        BlockState newState = this.stateDefinition.any();
        for ( Property<?> property : properties ) {
            newState = terrain_slabs$coerceProperty( property, oldState, newState );
        }
        return newState.setValue( offsetProperty, OffsetProperty.OffsetType.NONE );
    }

    @Unique
    private <T extends Comparable<T>> BlockState terrain_slabs$coerceProperty(Property<T> property, BlockState reference, BlockState state ) {
        return state.setValue( property, reference.getValue( property ) );
    }

    @Unique
    private Property<OffsetProperty.OffsetType> terrain_slabs$getNewProperty( boolean includeOntop, boolean includeOnbottom ) {
        if ( !includeOntop && !includeOnbottom ) {
            return null;
        }

        // Exit if no work to be done
        EnumProperty<?> offsetProperty = ((IOffsetState) this.defaultBlockState()).terrain_slabs$getOffsetProperty();
        if ( offsetProperty != null ) {
            if ( offsetProperty.equals( OffsetProperty.ALL ) ) {
                return null;
            }
            if ( includeOntop ) {
                if ( offsetProperty.equals(OffsetProperty.ONTOP) && !includeOnbottom ) {
                    return null;
                }
            } else if ( offsetProperty.equals( OffsetProperty.ONBOTTOM ) ) {
                return null;
            }
        }

        includeOntop = includeOntop || offsetProperty == OffsetProperty.ONTOP;
        includeOnbottom = includeOnbottom || offsetProperty == OffsetProperty.ONBOTTOM;
        return includeOnbottom ? ( includeOntop ? OffsetProperty.ALL : OffsetProperty.ONBOTTOM ) : OffsetProperty.ONTOP ;
    }

    @Shadow
    protected abstract void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

    @Shadow
    @Final
    protected abstract void registerDefaultState(BlockState state);
}
