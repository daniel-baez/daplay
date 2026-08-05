(ns daplay.css
  "Build entrypoint: compile Garden styles to assets/css/daplay.css."
  (:require [clojure.java.io :as io]
            [daplay.styles :as styles]))

(def output-path "assets/css/daplay.css")

(defn write!
  "Generate assets/css/daplay.css from daplay.styles."
  [& _]
  (let [out (io/file output-path)]
    (io/make-parents out)
    (spit out (styles/stylesheet))
    (println "Wrote" (.getCanonicalPath out))
    out))

(defn -main
  [& _args]
  (write!)
  (System/exit 0))
