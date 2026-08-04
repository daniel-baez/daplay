---
layout: app
title: About
permalink: /about/
---

**daplay** is a small playground where content stays as Markdown and the
interface is owned by ClojureScript.

Resource tiles live under `_tiles/` as ordinary Markdown files. At build time
Jekyll exports their *raw* Markdown into `/assets/data/site.json`. A
shadow-cljs app fetches that data and draws the landing board — typography,
motion, and all.

### Stack

- **Content**: Jekyll 4 (Markdown + front matter + `tiles` collection)
- **UI**: ClojureScript, Reagent, shadow-cljs
- **Markdown → HTML**: markdown-it in the browser
