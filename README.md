# daplay

Personal site for [daplay.cl](https://daplay.cl).

**Markdown in Jekyll → JSON data → ClojureScript UI** (shadow-cljs + Reagent).

## Dev

```bash
npm install
npm run cljs:compile          # Garden CSS + shadow-cljs; or: npm run cljs:watch
bundle install
bundle exec jekyll serve
```

Styles are written in Clojure with [Garden](https://github.com/noprompt/garden)
(`src/main/daplay/styles.clj`) and compiled to `assets/css/daplay.css`.

Or with Nix: `nix develop` then `serve`.
