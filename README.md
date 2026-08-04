# daplay

Personal site for [daplay.cl](https://daplay.cl).

**Markdown in Jekyll → JSON data → ClojureScript UI** (shadow-cljs + Reagent).

## Dev

```bash
npm install
npx shadow-cljs compile app   # or: npm run cljs:watch
bundle install
bundle exec jekyll serve
```

Or with Nix: `nix develop` then `serve`.
