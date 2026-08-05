# AGENTS.md

## Cursor Cloud specific instructions

This repository is a **Jekyll + ClojureScript** site deployed via GitHub Pages
(see `CNAME` → `daplay.cl`).

- **Content (Jekyll)**: Markdown tiles in `_tiles/` (landing board), plus
  `_posts/` and pages (`about.markdown`, …). Jekyll stashes raw Markdown
  (`_plugins/stash_raw_markdown.rb`) and exports it as `/assets/data/site.json`.
  Liquid/minima are not the UI layer.
- **UI (ClojureScript)**: `src/main/daplay/` compiled by **shadow-cljs** into
  `assets/js/`. Reagent mounts on `#app` (`_layouts/app.html`) and renders from
  the JSON data (markdown-it in the browser). The home route draws a tile grid.
- **Key files**: `_config.yml`, `_layouts/app.html`, `assets/data/site.json`,
  `assets/css/daplay.css`, `_tiles/`, `shadow-cljs.edn`, `package.json`, `Gemfile`.
- **Build output**: `_site/` (gitignored). Also gitignored: `node_modules/`,
  `.shadow-cljs/`, `assets/js/`, `vendor/bundle`, Jekyll caches.

### Running the site

The `flake.nix` dev shell provides Ruby, bundler, Node, JDK 21, and helpers.

- `nix develop` → then `serve` (bundle/npm if needed, `shadow-cljs compile app`,
 then `jekyll serve --host 0.0.0.0` on port `4000`).
- `build-site` / `npm run cljs:release` → Closure `:advanced` production JS
 (hashed `main.<hash>.js` via `:module-hash-names`) + `jekyll build`.
- `test-site` / `npm test` → ClojureScript node tests (`src/test/`, shadow-cljs
 `:test` build).
- For iterative CLJS work: `npm run cljs:watch` in one terminal, `serve` (or
 jekyll serve) in another.
- Without nix: `npm install && npx shadow-cljs compile app && bundle exec jekyll serve`.
- CI: `.github/workflows/ci.yml` runs tests on PRs; `pages.yml` runs
 test → build → deploy on `main`.

### Nix + direnv workflow

- Flakes must be enabled. With [direnv](https://direnv.net) + [nix-direnv](https://github.com/nix-community/nix-direnv): run `direnv allow` once and the dev shell auto-loads on `cd`. `use flake` in `.envrc` requires nix-direnv sourced from your `direnvrc`.
- Flakes only see git-tracked files — `git add` new files before `nix develop`/`direnv` picks them up.
- On a VM without systemd, start the Nix daemon manually (`sudo nix-daemon &`) before using `nix`.

### Notes

- First gem/npm/cljs install is slow (native gems + Maven deps for shadow-cljs).
- `_config.yml` is **not** hot-reloaded by `jekyll serve`; restart after editing it.
- Deployment: `.github/workflows/pages.yml` builds CLJS + Jekyll 4 and deploys
  to GitHub Pages (classic Pages Jekyll 3 is not used).
