(ns daplay.markdown
  (:require ["markdown-it" :as markdown-it]))

(def ^:private md
  (markdown-it.
   #js {:html false
        :linkify true
        :typographer true
        :breaks false}))

;; Resource lists are mostly external; open them in a new tab.
(set! (.. md -renderer -rules -link_open)
      (fn [tokens idx options env self]
        (let [token (aget tokens idx)
              href (.attrGet token "href")]
          (when (and href (re-find #"^https?://" href))
            (.attrSet token "target" "_blank")
            (.attrSet token "rel" "noopener noreferrer"))
          (.renderToken self tokens idx options))))

(defn render [markdown]
  (when (string? markdown)
    (.render md markdown)))
