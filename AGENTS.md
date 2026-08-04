# AGENTS.md

## Cursor Cloud specific instructions

This repository is a **Jekyll site** (minima theme) deployed via GitHub Pages
(see `CNAME` → `daplay.cl`). It was scaffolded with `jekyll new`.

- **Key files**: `_config.yml` (site config), `index.markdown`, `about.markdown`, `404.html`, `_posts/` (blog posts), `Gemfile` / `Gemfile.lock` (Ruby deps: `jekyll` 4.x + `minima` + `jekyll-feed`).
- **Build output**: `_site/` (gitignored). Caches `.jekyll-cache/`, `.sass-cache/` and gems in `vendor/` are gitignored too.
- **Gems are installed locally** into `vendor/bundle` (via `BUNDLE_PATH`), never globally.

### Running the site

The `flake.nix` dev shell provides `ruby`, `bundler`, a C toolchain, and a `serve` helper.

- `nix develop` → then `serve` (runs `bundle install` if needed, then `bundle exec jekyll serve --host 0.0.0.0`, on port `4000`). Pass Jekyll args through, e.g. `serve --livereload` or `serve --port 4001`.
- `nix run` (or `nix run .`) starts the same Jekyll server without entering the shell.
- Without the helper: `bundle install` then `bundle exec jekyll serve` from the repo root, then open `http://localhost:4000/`.

### Nix + direnv workflow

- Flakes must be enabled. With [direnv](https://direnv.net) + [nix-direnv](https://github.com/nix-community/nix-direnv): run `direnv allow` once and the dev shell auto-loads on `cd`. `use flake` in `.envrc` requires nix-direnv sourced from your `direnvrc`.
- Flakes only see git-tracked files — `git add` new files before `nix develop`/`direnv` picks them up.
- On a VM without systemd, start the Nix daemon manually (`sudo nix-daemon &`) before using `nix`.

### Notes

- First `serve`/`nix run` compiles/downloads native gems (e.g. `sass-embedded`, `google-protobuf`) into `vendor/bundle`; the C toolchain in the flake covers this. Subsequent runs are fast.
- The minima theme emits harmless Sass `lighten()` deprecation warnings on build — safe to ignore.
- `_config.yml` is **not** hot-reloaded by `jekyll serve`; restart the server after editing it.
- **Deployment note**: the scaffold pins Jekyll 4.x. GitHub Pages' classic (automatic) build uses Jekyll 3.x, so to deploy this on Pages use a GitHub Actions Pages workflow (or switch the `Gemfile` to the `github-pages` gem). No CI workflow is committed yet.
