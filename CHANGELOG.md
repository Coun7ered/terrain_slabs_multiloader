# Changelog

## [4.0.4-beta]

Various cleanup and fixes.

### Added
- Regenerate invalid configs
- Reorganize / simplify slab classes, add API classes
- ITillable API interface (currently unused)

### Fixed
- Update to fixed Poly Mixin version for Fabric 1.20.1 (less unstable)
- Fix floating torches (or others using support shape methods)
- Fix invalid config file crash

***

## [4.0.3-beta]

Add Poly Mixin dependency for better automatic compatibility.
Add options to API

***

## [4.0.2-beta]

Small Bugfix (some items get wrong description ID)

### Fixed
- Fix unsafe call to Block.getDescriptionId()

## [4.0.1-beta]

You might have to delete an existing old config file "terrain_slabs.json" to not get a crash on startup

### Fixed
- various fixes

## [4.0.0-beta]

This version contains breaking changes so consider making a backup before updating

You might also have to delete an existing old config file "terrain_slabs.json" to not get a crash on startup

### Added
- Implement offset with blockstates
- allow ontop and onbottom offsets and all that implies
- implement nice falling sand and water breaking slabs (configurable, disabled with fluidlogged)
- Implement api methods and interfaces for compatibility
- Implement configs for allowing/forbidding offsets on blocks

## [3.1.2]

### Fixed
- fix waterlogged slabs in basalt deltas

## [3.1.1]

### Fixed
- fix log spam

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