package net.countered.terrainslabs.mixin_applier;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static net.countered.terrainslabs.mixin_applier.EarlyConfigReader.CTS_CONFIGS;

@SuppressWarnings("unused")
public final class TerrainSlabsMixinPlugin implements IMixinConfigPlugin {
    private static final List<String> ONTOP_VEGETATION_MIXIN_CLASSES = List.of(
            "net.countered.terrainslabs.mixin.ontop.place.MixinBlockBehaviours",
            "net.countered.terrainslabs.mixin.ontop.render.MixinBlockModelShaper",
            "net.countered.terrainslabs.mixin.ontop.render.MixinLevelRenderer",
            "net.countered.terrainslabs.mixin.ontop.state.MixinBlock",
            "net.countered.terrainslabs.mixin.ontop.state.MixinBlockState",
            "net.countered.terrainslabs.mixin.ontop.state.MixinBlockStateBase"
    );

    @Override
    public void onLoad( String s ) {}

    /**
     * Disables vegetation mixins on load instead of during play.
     * TODO: make this correct for snow and etc
     * TODO: much of this can be implemented in better ways, i.e. ASM / state assignment
     * TODO: apply more configs
     */
    @Override
    public boolean shouldApplyMixin( String targetClassName, String mixinClassName ) {
        assert CTS_CONFIGS != null;
        if ( !CTS_CONFIGS.enableSnowOnSlabs() && !CTS_CONFIGS.enableVegetationOnSlabs() ) {
            return !mixinClassName.equals("net.countered.terrainslabs.mixin.ontop.render.MixinBlockStateBaseOcclusion")
                    || !ONTOP_VEGETATION_MIXIN_CLASSES.contains(mixinClassName);
        } else if ( !CTS_CONFIGS.enableSnowOnSlabs() ) {
            return !mixinClassName.equals("net.countered.terrainslabs.mixin.ontop.render.MixinBlockStateBaseOcclusion");
        }

        return true;
    }

    public String getRefMapperConfig() {return null;}
    public void acceptTargets(Set<String> set, Set<String> set1) {}
    public List<String> getMixins() {return null;}
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}

