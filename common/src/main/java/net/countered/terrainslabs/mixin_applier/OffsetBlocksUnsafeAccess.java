package net.countered.terrainslabs.mixin_applier;

import net.countered.terrainslabs.api.OffsetBlocks;
import net.countered.terrainslabs.util.BlockChecker;
import net.minecraft.world.level.block.Block;

public class OffsetBlocksUnsafeAccess extends OffsetBlocks {

    public static boolean isIncludedOntop(Block block) {
        if (apiIncludeOntopCheck == null) {
            apiIncludeOntopCheck = new BlockChecker(API_INCLUDED_ONTOP_BLOCKS);
        }

        return (apiIncludeOntopCheck.contains(block) || EarlyConfigReader.ONTOP_INCLUDE.contains(block))
                && !isExcludedOntop(block);
    }

    public static boolean isExcludedOntop(Block block) {
        if (apiExcludeOntopCheck == null) {
            apiExcludeOntopCheck = new BlockChecker(API_EXCLUDED_ONTOP_BLOCKS);
        }

        return EarlyConfigReader.ONTOP_EXCLUDE.contains(block)
                || (!EarlyConfigReader.ONTOP_INCLUDE.contains(block) && apiExcludeOntopCheck.contains(block));
    }

    public static boolean isIncludedOnbottom(Block block) {
        if (apiIncludeOnbottomCheck == null) {
            apiIncludeOnbottomCheck = new BlockChecker(API_INCLUDED_ONTOP_BLOCKS);
        }

        return (apiIncludeOnbottomCheck.contains(block) || EarlyConfigReader.ONBOTTOM_INCLUDE.contains(block))
                && !isExcludedOnbottom(block);
    }

    public static boolean isExcludedOnbottom(Block block) {
        if (apiExcludeOnbottomCheck == null) {
            apiExcludeOnbottomCheck = new BlockChecker(API_EXCLUDED_ONBOTTOM_BLOCKS);
        }

        return EarlyConfigReader.ONBOTTOM_EXCLUDE.contains(block)
                || (!EarlyConfigReader.ONBOTTOM_INCLUDE.contains(block) && apiExcludeOnbottomCheck.contains(block));
    }
}
