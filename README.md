# Countered's Terrain Slabs (Multiloader)
## Overview

Countered's Terrain Slabs is a Minecraft modification designed to improve world exploration. It introduces dynamic slabs to the terrain generation, resulting in smoother landscapes and more natural slopes. By reducing the frequency of full-block jumps while climbing hills or mountains, the mod creates a more fluid movement experience without altering the core aesthetic of the game.

This version is a complete rewrite of the original Fabric-only mod, now utilizing a Multiloader architecture to support multiple mod loaders from a single codebase.

## Development and Contribution Rules

To maintain code quality and avoid versioning conflicts, please adhere to the following workflow when contributing:
### Branch Structure

- main: This is the default branch. It contains the most recent stable code for the latest supported Minecraft version.

- Version Branches (e.g., 1.20.x): These branches are dedicated to older Minecraft versions. They are maintained for critical bug fixes only.

### Pull Requests (PR)

- Target Branch: All feature requests and general improvements must be targeted at the main branch.

- Version Ports: If you wish to port the mod to a new Minecraft version that does not yet have a branch, please open an Issue first or submit the PR to main for discussion.

- Bug Fixes: If you are fixing a bug specific to an older version, you may target the corresponding version branch.

- Consistency: Ensure that changes to core logic are made within the common module to keep all loaders in sync.
