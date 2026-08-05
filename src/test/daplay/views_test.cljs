(ns daplay.views-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.string :as str]
            [reagent.dom.server :as rds]
            [daplay.views :as views]))

(defn- html [comp]
  (rds/render-to-static-markup comp))

(def ^:private sample-site
  {:title "daplay"
   :github_repo "daniel-baez/daplay"
   :github_username "daniel-baez"
   :twitter_username "daplay"
   :linkedin_username "baezdaniel"})

(def ^:private sample-posts
  [{:title "First note"
    :url "/2024/01/01/first-note/"
    :date "2024-01-01T00:00:00.000Z"
    :excerpt "A short excerpt"
    :markdown "# First note\n\nBody."}])

(def ^:private sample-tiles
  [{:title "Clojure"
    :url "/resources/clojure/"
    :summary "Links about Clojure"
    :markdown "- [Clojure](https://clojure.org)"}])

(def ^:private sample-pages
  [{:title "About"
    :url "/about/"
    :markdown "Hello about."}])

(defn- shell [overrides]
  (html [views/shell
         (merge {:status :ready
                 :route "/"
                 :site sample-site
                 :posts sample-posts
                 :pages sample-pages
                 :tiles sample-tiles
                 :error nil}
                overrides)]))

(deftest shell-loading
  (let [h (shell {:status :loading})]
    (is (str/includes? h "Loading daplay"))))

(deftest shell-error
  (let [h (shell {:status :error :error "boom"})]
    (is (str/includes? h "Could not load site data."))
    (is (str/includes? h "boom"))))

(deftest shell-home-lists-posts
  (let [h (shell {:route "/"})]
    (is (str/includes? h "First note"))
    (is (str/includes? h "A short excerpt"))
    (is (str/includes? h "href=\"/2024/01/01/first-note/\""))))

(deftest shell-home-empty-state
  (let [h (shell {:route "/" :posts []})]
    (is (str/includes? h "No posts yet"))
    (is (str/includes? h "href=\"/resources/\""))))

(deftest shell-resources-board
  (let [h (shell {:route "/resources/"})]
    (is (str/includes? h "Resources"))
    (is (str/includes? h "Clojure"))
    (is (str/includes? h "Links about Clojure"))))

(deftest shell-routes-content-pages
  (testing "post page"
    (let [h (shell {:route "/2024/01/01/first-note/"})]
      (is (str/includes? h "First note"))
      (is (str/includes? h "← Blog"))))
  (testing "tile page"
    (let [h (shell {:route "/resources/clojure/"})]
      (is (str/includes? h "Clojure"))
      (is (str/includes? h "← Resources"))))
  (testing "static page"
    (let [h (shell {:route "/about/"})]
      (is (str/includes? h "About"))
      (is (str/includes? h "Hello about.")))))

(deftest shell-not-found
  (let [h (shell {:route "/missing/"})]
    (is (str/includes? h "404"))
    (is (str/includes? h "Nothing at this path."))))

(deftest shell-chrome
  (let [h (shell {:route "/"})]
    (is (str/includes? h "Fork me on GitHub"))
    (is (str/includes? h "href=\"https://github.com/daniel-baez/daplay\""))
    (is (str/includes? h "aria-label=\"Primary\""))
    (is (str/includes? h "daplay.cl"))))
