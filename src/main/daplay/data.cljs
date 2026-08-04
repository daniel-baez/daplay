(ns daplay.data)

(defn- js->clj-kw [x]
  (js->clj x :keywordize-keys true))

(defn fetch-site!
  "Load site JSON produced by Jekyll from raw Markdown sources.
  Returns a Promise that resolves to [:ok data] or [:error message]."
  [url]
  (-> (js/fetch url)
      (.then (fn [res]
               (if-not (.-ok res)
                 (throw (js/Error. (str "Failed to load " url " (" (.-status res) ")")))
                 (.json res))))
      (.then (fn [data]
               [:ok (js->clj-kw data)]))
      (.catch (fn [e]
                [:error (or (.-message e) (str e))]))))
