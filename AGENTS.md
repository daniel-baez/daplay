# AGENTS.md

## Cursor Cloud specific instructions

This repository is a **static website** (a Bootstrap 5 starter page) deployed via GitHub Pages
(see `CNAME` → `daplay.cl`). There is no package manager, build step, test suite, or backend.

- **Files**: `index.html` is the entire app. It pulls Bootstrap CSS/JS from the jsDelivr CDN.
- **Run (dev)**: serve the folder statically, e.g. `python3 -m http.server 8000` from the repo root, then open `http://localhost:8000/`.

### Nix + direnv workflow

The repo ships a `flake.nix` and `.envrc` for a reproducible dev environment.

- With [Nix](https://nixos.org) (flakes enabled): `nix develop` drops you into a shell with `python3`, `git`, and a `serve` helper on `PATH`. `serve [port]` (default `8000`) serves the static site.
- `nix run` (or `nix run . -- <port>`) starts the static server directly without entering the dev shell.
- With [direnv](https://direnv.net) + [nix-direnv](https://github.com/nix-community/nix-direnv): run `direnv allow` once; the flake dev shell then loads automatically whenever you `cd` into the repo. `use flake` in `.envrc` requires nix-direnv to be sourced from your `direnvrc`.
- `.direnv/` is a local cache and is gitignored. Flakes only see git-tracked files, so `git add` new files before `nix develop`/`direnv` will pick them up.
- **Expected 404s**: `index.html` references `styles.css` and `main.js`, which do not exist in the repo (leftover placeholders from the Bootstrap starter template). Their 404s are harmless — do not add these files unless the task requires it.
- **Lint / test / build**: none exist. There is nothing to install or compile.
