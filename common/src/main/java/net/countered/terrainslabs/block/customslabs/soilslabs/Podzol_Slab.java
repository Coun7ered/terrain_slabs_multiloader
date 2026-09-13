package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.world.level.block.Block;

public final class Podzol_Slab extends SnowySpreadableSlab {
    public Podzol_Slab(Block block, ISlabCopy duel) {
        super(block, duel);
    }

    @Override
    protected boolean canSpread() {
        return false;
    }
}
