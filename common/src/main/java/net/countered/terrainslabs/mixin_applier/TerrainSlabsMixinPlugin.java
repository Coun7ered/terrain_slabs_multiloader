package net.countered.terrainslabs.mixin_applier;

import dev.architectury.platform.Platform;
import net.countered.terrainslabs.mixin.offset.state.MixinStateDefinition;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static net.countered.terrainslabs.mixin_applier.EarlyConfigReader.CTS_CONFIGS;

/**
 * Coarse control over mixin functionality and compatibility through Mixin interface
 */
@SuppressWarnings("unused")
public final class TerrainSlabsMixinPlugin implements IMixinConfigPlugin {
    private static final List<String> CORE_OFFSET_CLASSES = List.of(
            "net.countered.terrainslabs.mixin.offset.state.MixinStateDefinition",
            "net.countered.terrainslabs.mixin.offset.state.MixinBlock",
            "net.countered.terrainslabs.mixin.offset.state.MixinBlockState",
            "net.countered.terrainslabs.mixin.offset.render.target.MixinRaytrace",
            "net.countered.terrainslabs.mixin.offset.render.MixinBlockStateBase",
            "net.countered.terrainslabs.mixin.offset.place.FacedAssigner",
            "net.countered.terrainslabs.mixin.offset.place.MixinBlockItem",
            "net.countered.terrainslabs.mixin.offset.place.MixinBlocks"
    );

    /**
     * Disables vegetation mixins on load instead of during play.
     */
    @Override
    public boolean shouldApplyMixin( String targetClassName, String mixinClassName ) {
        assert CTS_CONFIGS != null;
        if ( CORE_OFFSET_CLASSES.contains(mixinClassName) ) {
            return CTS_CONFIGS.enableSnowOnSlabs() || CTS_CONFIGS.enableVegetationOnSlabs();
        } else if ( mixinClassName.equals("net.countered.terrainslabs.mixin.offset.render.MixinBlockStateBaseOcclusion") ) {
            return CTS_CONFIGS.enableSnowOnSlabs();
        } else if ( mixinClassName.equals("net.countered.terrainslabs.mixin.terrain.MixinFlowingFluid") ) {
            return CTS_CONFIGS.fluidsDestroyGeneration() && Platform.getOptionalMod("fluidlogged").isEmpty();
        } else if ( mixinClassName.matches( "net.countered.terrainslabs.mixin.offset.block_tweaks.MixinBaseFireBlock" ) ) {
            return CTS_CONFIGS.fireBlocksOffset();
        } else if ( mixinClassName.matches( "net.countered.terrainslabs.mixin.offset.block_tweaks.*" ) ) {
            return CTS_CONFIGS.enableVegetationOnSlabs();
        }

        return true;
    }

    @Override
    public void onLoad( String s ) {}
    @Override
    public List<String> getMixins() {return null;}
    @Override
    public String getRefMapperConfig() {return null;}
    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}

