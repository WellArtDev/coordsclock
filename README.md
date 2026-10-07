# CoordsClock

A tiny always-on HUD widget in the bottom-left corner: your block
coordinates, the direction you're facing, the in-game time and day, and
the current biome — zero config, just works.

## Features

- **Live coordinates** — "XYZ: 123 / 64 / -456" plus your facing
  direction, e.g. "(North)"
- **In-game clock** — day counter and 24-hour time, e.g. "Day 12 —
  14:30"
- **Biome readout** — prettified biome name, e.g. "Plains" or
  "Old Growth Pine Taiga"
- **Zero config** — no commands, no keybinds, no config file; it just
  shows up when you play

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.19.2+ for
   Minecraft 26.2
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for
   Minecraft 26.2
3. Drop `coordsclock-1.0.0.jar` into your `mods` folder
4. Launch and play — no config needed

## Notes

- Client-side only. Works on any server (including vanilla).
- No mixins, no networking, nothing saved to disk.

## License

MIT — see [LICENSE](LICENSE).
