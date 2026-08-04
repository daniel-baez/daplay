---
layout: app
title: "Welcome to daplay"
date: 2026-08-04 20:50:26 +0000
categories: jekyll cljs
---

This post lives in `_posts/` as ordinary Markdown. Jekyll does **not** turn it
into the page you are reading — it only publishes the raw source into
`/assets/data/site.json`. ClojureScript picks it up and renders it.

### Why this split?

- Keep writing in familiar Markdown files
- Own the UI with a modern ClojureScript toolchain (shadow-cljs + Reagent)
- Ship a fast static site without Liquid templates driving the look

Code fences work as you would expect:

```ruby
def print_hi(name)
  puts "Hi, #{name}"
end
print_hi("daplay")
```

Edit this file, rebuild, and the CLJS app will show your changes.
