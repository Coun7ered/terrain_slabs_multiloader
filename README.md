# Countered's Terrain Slabs API — Fork

## Overview

Countered's Terrain Slabs is a Minecraft mod that improves world exploration. It adds
dynamic slabs to terrain generation, producing smoother landscapes and more natural
slopes and reducing the frequency of full-block jumps while climbing hills or
mountains — all without altering the game's core aesthetic.

The upstream mod already uses an **Architectury multiloader** architecture (a single
`common` codebase compiled for both Fabric and NeoForge). This fork keeps that
architecture and builds on top of it, adding two things:

1. **A public addon/compat API** (`ModSlabsMap`) so other mods can register their own
   blocks with the terrain-slab generator.
2. **A "snow slab" feature** — a slab that carries snow layers on top, replacing the
   base mod's older offset-based snow handling.

## Project structure

Standard Architectury layout:

- `common/` — shared, loader-agnostic code: blocks, registries, world-gen
  (`SlabFeature`), mixins, and the public API. The vast majority of the mod lives here.
- `fabric/` — Fabric entry point, datagen providers, and Fabric-only mixins/rendering.
- `neoforge/` — NeoForge entry point and NeoForge-only mixins/rendering.

Loader-specific behavior that `common` needs is abstracted behind Architectury's
`@ExpectPlatform` (see `platform/PlatformConfigHooks`), with `…Impl` classes supplied
by each loader. Each loader's entry point (`TerrainSlabsFabric`,
`TerrainSlabsNeoForge`) calls the shared `TerrainSlabs.init()` and wires up its config
provider.

## What this fork adds

### 1. Public addon / compat API — `ModSlabsMap`

`net.countered.terrainslabs.block.ModSlabsMap` is a public, loader-agnostic API that
lets other mods hook into terrain-slab generation. The world-gen `SlabFeature` looks up
mappings from this map lazily at generation time, so **any mapping registered before the
first chunk generates takes effect** — call it from your addon's initialization (a
Fabric `ModInitializer` or a NeoForge mod constructor).

```java
// Map a source block to the slab that should generate from it.
ModSlabsMap.register(sourceBlock, yourSlabBlock);

// Same, but also mark it as a "soil" slab: the feature converts the block below to
// dirt before placing, and falls back to a plain dirt slab for the top variant —
// matching the built-in grass / podzol / mycelium / path slabs.
ModSlabsMap.registerSoilSlab(sourceBlock, yourSoilSlabBlock);
```

Key methods:

- `register(Block source, Block slab)` — register a slab to generate from `source`.
  Returns `true` for a new mapping, `false` if it replaced one. Your slab should extend
  `CustomSlab` so it has the required `generated` blockstate property.
- `registerSoilSlab(Block source, Block slab)` — as above, plus soil behavior
  (dirt conversion + dirt-slab top fallback).
- `getSlabForBlock(Block)` — the reverse lookup the feature uses (nullable).
- `isSoilSlab(Block)` — whether a slab is treated as soil (built-in or addon-registered).
- `getMappings()` — an unmodifiable view of all current mappings, for inspection.

This API is what the companion **BWG (Oh The Biomes We've Gone) compat addon**
(`terrain_slabs_bwg`) uses to register BWG's blocks without modifying the base mod.

### 2. Snow slab feature *(branch: `feat/snow-slab`, in progress)*

A dedicated block (`snow_slab_test` during development) that is a bottom slab in its
lower half and holds **1–4 snow layers** in its upper half, storing the original slab's
`BlockState` in a block entity so it renders, is named, and drops as that slab. It
replaces the base mod's older offset-based snow-on-slabs approach.

Behavior:

- **World-gen:** slabs in cold biomes generate snow-capped, hooked directly into
  `SlabFeature`. A late pass (after vanilla places ground snow) fills in slabs that
  border already-snowy full blocks and removes any vanilla snow layer that landed on
  top of a snow slab. Powder snow is never replaced or built under.
- **Weather:** mirrors vanilla snow accumulation (via a `tickChunk` mixin) and brings a
  bare slab up to a single snow layer.
- **Manual placement:** using the snow layer item on a bare slab creates the snow slab;
  further use stacks layers 2→4, then places/grows a normal vanilla snow layer above.
- **Rendering (Fabric):** a dynamic model renders the stored slab's real texture as the
  bottom half (cutout transparency for grass, snowy grass sides), with the snow layers
  on top.
- **Interaction:** region-aware — aiming at the snow vs. the base slab changes what
  pick-block returns and whether a break clears only the snow or removes the whole
  block.
- **HUD mods (Jade/WAILA/WTHIT):** the block reports its name as
  "`<stored slab>` with Snow" and picks/shows the stored slab, using only the vanilla
  `Nameable` path (no third-party dependency).

Current status: functional on **Fabric**. NeoForge dynamic bottom rendering and
NeoForge region-aware pick-block are not yet implemented. The development block name
keeps the `_test` suffix until the feature is finalized.

## Building

Architectury multiloader build:

```bash
# Fabric
./gradlew :fabric:build
# NeoForge
./gradlew :neoforge:build
# Regenerate data (tags, loot, models) after changing datagen providers
./gradlew :fabric:runDatagen
```

Put core, loader-agnostic logic in `common` so both loaders stay in sync; only truly
loader-specific code belongs in `fabric` / `neoforge`.

## Development and Contribution Rules

To maintain code quality and avoid versioning conflicts, please follow this workflow.

### Branch Structure

- **main** — the default branch, holding the most recent stable code for the latest
  supported Minecraft version.
- **Version branches** (e.g. `1.21.1`, `1.20.1`) — dedicated to specific/older Minecraft
  versions; maintained for critical bug fixes.
- **Feature branches** (e.g. `feat/snow-slab`) — in-progress features not yet merged.

### Pull Requests

- **Target branch:** feature requests and general improvements target `main`.
- **Version ports:** to port to a Minecraft version without a branch, open an Issue
  first, or submit the PR to `main` for discussion.
- **Bug fixes:** version-specific fixes may target the corresponding version branch.
- **Consistency:** make changes to core logic in the `common` module so all loaders stay
  in sync.

## Credits

- Original mod: **Countered's Terrain Slabs** (Architectury multiloader).
- This fork: addon/compat API (`ModSlabsMap`) and the snow slab feature.
