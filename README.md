# Worley's Caves Reforged

A modern fork of [Worley's Caves](https://www.curseforge.com/minecraft/mc-mods/worleys-caves) by **SuperFluke** and **booleanbyte**.

Replaces vanilla cave generation with Worley (cellular) noise.

Fabric additionally requires **Fabric API**.

## Configuration

| Loader | File |
|---|---|
| Forge / NeoForge | `config/worleycaves-common.toml` |
| Fabric | `config/worleycaves.json` |

All values require a world restart to take effect.

| Option | Default | Meaning |
|---|---|---|
| `lavaDepth` | `-54` | Air at or below this Y becomes `lavaBlock` |
| `easeInDepth` | `15` | Caves thin out within this many blocks of the surface |
| `lavaBlock` | `minecraft:lava` | Block used for the deep lava layer |
| `maxCaveHeight` | `128` | Caves never generate above this Y |
| `minCaveHeight` | `-56` | Caves never generate below this Y |
| `noiseCutoffValue` | `-0.18` | Cave size. Smaller means larger caves |
| `surfaceCutoffValue` | `-0.081` | Cave size at the surface. Smaller means more surface openings |
| `verticalCompressionMultiplier` | `2.0` | Squishes caves on Y. Lower means taller caves |
| `horizonalCompressionMultiplier` | `1.0` | Stretches (<1) or compresses (>1) caves on X/Z |
| `warpAmplifier` | `8.0` | Cave warping. Lower means straighter tunnels |

`lavaDepth` and `minCaveHeight` default to the deepslate layer rather than the original's `10`/`1`,
since the modern world floor is Y=-64.

### Making modded stone carvable

The carver digs through vanilla stone, dirt, sand, gravel, terracotta and the usual overworld
surface blocks, plus anything in `#forge:stone` / `#c:stone`. To add more, define the block tag
`#worleycaves:carvable` in a datapack.


Cave *shape* is unchanged. The noise sampling and interpolation are exactly the same.

## Credits

- **SuperFluke**, **booleanbyte** original Worley's Caves.
- `FastNoise` by Jordan Peck (MIT)
