(ns daplay.markdown
  (:require ["markdown-it" :as markdown-it]))

(def ^:private md
  (markdown-it.
   #js {:html false
        :linkify true
        :typographer true
        :breaks false}))

(defn render [markdown]
  (when (string? markdown)
    (.render md markdown)))
