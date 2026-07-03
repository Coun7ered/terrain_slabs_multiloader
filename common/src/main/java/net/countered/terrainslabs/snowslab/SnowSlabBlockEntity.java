package net.countered.terrainslabs.snowslab;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.Nameable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Stores the BlockState of the slab sitting underneath the snow, so the combined
 * "snow slab" can render, drop, and behave faithfully for ANY bottom slab.
 *
 * This is the same idea DoubleSlabs uses (a BlockEntity holding an arbitrary
 * BlockState) — it is the only way to support arbitrary/modded bottom slabs while
 * keeping their real texture and properties.
 *
 * NOTE: This is the standalone TEST version. It is wired ONLY to SnowSlabBlock and
 * does not touch the mod's existing offset-hack snow mixins.
 */
public class SnowSlabBlockEntity extends BlockEntity implements Nameable {

    private static final String KEY_SLAB = "BottomSlab";

    // The slab BlockState shown/used as the bottom half. Defaults to a stone slab
    // so the block is never in a broken/empty state.
    private BlockState bottomSlab = Blocks.SMOOTH_STONE_SLAB.defaultBlockState();

    public SnowSlabBlockEntity(BlockPos pos, BlockState state) {
        super(SnowSlabRegistry.SNOW_SLAB_BE.get(), pos, state);
    }

    public BlockState getBottomSlab() {
        return bottomSlab;
    }

    public void setBottomSlab(BlockState state) {
        this.bottomSlab = state;
        setChanged();
        if (level != null && !level.isClientSide) {
            // Push to clients so the renderer (added later) and any client logic see it.
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(KEY_SLAB)) {
            // NbtUtils handles unknown/old blockstates gracefully (falls back to air),
            // so guard against that turning into an invalid bottom.
            BlockState read = NbtUtils.readBlockState(provider.lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), tag.getCompound(KEY_SLAB));
            if (!read.isAir()) {
                this.bottomSlab = read;
            }
        }
        // The per-block BE update packet routes through here on the client; re-bake
        // so the model shows the stored slab immediately.
        requestClientRerender();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(KEY_SLAB, NbtUtils.writeBlockState(bottomSlab));
    }

    // --- Client sync: ship the stored slab to the client for rendering ---

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.put(KEY_SLAB, NbtUtils.writeBlockState(bottomSlab));
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }



    private void requestClientRerender() {
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
                    Block.UPDATE_ALL);
        }
    }


    // --- Nameable: report the stored slab's name so Jade/WAILA/WTHIT show e.g.
    // "Terrain Granite Slab" instead of the snow slab's own name (no Jade dependency). ---

    @Override
    public Component getName() {
        return withSnow(bottomSlab.getBlock().getName());
    }

    @Override
    public boolean hasCustomName() {
        return !bottomSlab.isAir();
    }

    @Override
    public Component getCustomName() {
        return bottomSlab.isAir() ? null : withSnow(bottomSlab.getBlock().getName());
    }

    /**
     * Formats the stored slab's name as "&lt;Slab&gt; with Snow" via a translatable
     * key so it localizes. Key: block.terrain_slabs.snow_slab_test.with_snow
     * ("%s with Snow"), %s = the base slab's name.
     */
    private static Component withSnow(Component base) {
        return Component.translatable("block.terrain_slabs.snow_slab_test.with_snow", base);
    }

}
