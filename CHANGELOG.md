# Changelog

## v3.4.1

### Fixed
- Slabs no longer replace powder snow
- Readded option to reduce slab ambient occlusion strength. Default 0.5

***

## v3.4.0

### Added
- New option to enable automatic slab matching. When playing with modded terrain and a slab variant for a block exists,
it will try to use it in the generation process.

### Fixed
- Slabs no longer generate next to lava in basalt deltas or next to water in modded overworld biomes.

***

## v3.3.6

### Fixed
- Top of grass slab textures is no longer transparent when "Better Grass Slabs" resource pack is enabled (by @nsacat)
- Non-full slabs (mud, farmland...) no longer occlude the view when crawling over them or ducking below  (by @nsacat)

***

## v3.3.5

### Fixed
- fix non-full-slabs allowing occlusion (causing grass to not turn into dirt) 
- fix specific generated slabs not dropping their block counterpart
- fix large spruce trees not turning grass slabs to podzol

***

## v3.3.4

### Changed
- Port Fabric and NeoForge to Minecraft 26.3.
- Update slab world generation and tool interactions for the 26.3 APIs.
- Migrate loot tables, recipe advancements and block tags to the 26.3 data format.
- Update the built-in resource pack format for Minecraft 26.3.
- Bundle MixinExtras 0.5.5 on NeoForge for compatibility with the updated Mixin annotations.

***

## v3.3.3

### Fixed
- fix crash caused by grass spreading 

***

## v3.3.2

### Fixed
- fix soil slabs not dropping dirt slabs when mined
- fix path slabs not turning to dirt with block above

***

## [3.3.1]

### Fixed 
- fix waterlogged slabs in basalt deltas 

## [3.3.0]

### Added
- add farmland slabs

### Fixed
- fix grass spreading between slabs and blocks
- fix fabric dedicated server crash

## [3.2.0]
Many internal improvements

### Fixed

- Snow can no longer generate on ice slabs

### Changed

- Default slab run length is now 2 blocks

## [3.1.0]
### Added
Following experimental options have been added to the config:
- add support for corner slabs
- add support for slab run length

## [3.0.5]
### Fixed
- fix blocks on slabs not rendering correctly + related crashes

### Fixed

## [3.0.4]

### Fixed
- fix random waterlogged slabs
- fix game freeze when playing with other world generation mods

## [3.0.3]

### Fixed
- fix trees generating on slabs
