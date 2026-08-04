# AGENTS.md

## Cursor Cloud specific instructions

This repository is a **static website** (a Bootstrap 5 starter page) deployed via GitHub Pages
(see `CNAME` → `daplay.cl`). There is no package manager, build step, test suite, or backend.

- **Files**: `index.html` is the entire app. It pulls Bootstrap CSS/JS from the jsDelivr CDN.
- **Run (dev)**: serve the folder statically, e.g. `python3 -m http.server 8000` from the repo root, then open `http://localhost:8000/`.
- **Expected 404s**: `index.html` references `styles.css` and `main.js`, which do not exist in the repo (leftover placeholders from the Bootstrap starter template). Their 404s are harmless — do not add these files unless the task requires it.
- **Lint / test / build**: none exist. There is nothing to install or compile.
