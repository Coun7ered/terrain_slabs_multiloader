package net.countered.terrainslabs.api;

import net.countered.terrainslabs.util.BlockChecker;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class OffsetBlocks {

    //Does not combine with config. Config takes precedent.
    protected static final List<String> API_INCLUDED_ONTOP_BLOCKS = new ArrayList<>();
    protected static BlockChecker apiIncludeOntopCheck;

    //Does not combine with config. Config takes precedent.
    protected static final List<String> API_EXCLUDED_ONTOP_BLOCKS = new ArrayList<>();
    protected static BlockChecker apiExcludeOntopCheck;

    //Does not combine with config. Config takes precedent.
    protected static final List<String> API_INCLUDED_ONBOTTOM_BLOCKS = new ArrayList<>();
    protected static BlockChecker apiIncludeOnbottomCheck;

    //Does not combine with config. Config takes precedent.
    protected static final List<String> API_EXCLUDED_ONBOTTOM_BLOCKS = new ArrayList<>();
    protected static BlockChecker apiExcludeOnbottomCheck;


    //========================//
    // Data Retrieval Methods //
    //========================//


    public static boolean isApiIncludedOntop(Block block) {
        return (!isApiExcludedOntop(block)) && apiIncludeOntopCheck != null ? apiIncludeOntopCheck.contains(block)
                : API_INCLUDED_ONTOP_BLOCKS.contains(BlockChecker.safeGetDescriptionId(block));
    }

    public static boolean isApiExcludedOntop(Block block) {
        return apiExcludeOntopCheck != null ? apiExcludeOntopCheck.contains(block)
                : API_EXCLUDED_ONTOP_BLOCKS.contains(BlockChecker.safeGetDescriptionId(block));
    }

    public static boolean isApiIncludedOnbottom(Block block) {
        return (!isApiExcludedOnbottom(block)) && apiIncludeOnbottomCheck != null ? apiIncludeOnbottomCheck.contains(block)
                : API_INCLUDED_ONBOTTOM_BLOCKS.contains(BlockChecker.safeGetDescriptionId(block));
    }

    public static boolean isApiExcludedOnbottom(Block block) {
        return apiExcludeOnbottomCheck != null ? apiExcludeOnbottomCheck.contains(block)
                : API_EXCLUDED_ONBOTTOM_BLOCKS.contains(BlockChecker.safeGetDescriptionId(block));
    }


    //====================//
    // Actual API Methods //
    //====================//



    public static boolean includeOntopBlock(String id) {
        if (apiIncludeOntopCheck != null) {
            return false;
        }

        return API_INCLUDED_ONTOP_BLOCKS.add(id);
    }

    public static boolean excludeOntopBlock(String id) {
        if (apiExcludeOntopCheck != null) {
            return false;
        }

        return API_EXCLUDED_ONTOP_BLOCKS.add(id);
    }

    public static boolean includeOnbottomBlock(String id) {
        if (apiIncludeOnbottomCheck != null) {
            return false;
        }

        return API_INCLUDED_ONBOTTOM_BLOCKS.add(id);
    }

    public static boolean excludeOnbottomBlock(String id) {
        if (apiExcludeOnbottomCheck != null) {
            return false;
        }

        return API_EXCLUDED_ONBOTTOM_BLOCKS.add(id);
    }
}
