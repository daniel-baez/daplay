---
layout: app
title: About
permalink: /about/
---

**daplay** is a small playground where content stays as Markdown and the
interface is owned by ClojureScript.

Posts and pages live under Jekyll (`_posts/`, pages). At build time Jekyll
exports their *raw* Markdown as JSON. A shadow-cljs app fetches that data and
renders the site — typography, motion, and all.

### Stack

- **Content**: Jekyll 4 (Markdown + front matter)
- **UI**: ClojureScript, Reagent, shadow-cljs
- **Markdown → HTML**: markdown-it in the browser
