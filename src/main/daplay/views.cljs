(ns daplay.views
  (:require [daplay.markdown :as md]))

(defn- format-date [iso]
  (when iso
    (let [d (js/Date. iso)]
      (.toLocaleDateString d "en-GB"
       #js {:year "numeric" :month "short" :day "numeric"}))))

(defn- brand-mark []
  [:a.brand {:href "/"}
   [:span.brand__mark "da"]
   [:span.brand__play "play"]])

(defn- github-mark []
  [:svg.forkme__icon
   {:viewBox "0 0 16 16"
    :aria-hidden "true"
    :focusable "false"}
   [:path
    {:fill "currentColor"
     :d (str "M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38"
             " 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13"
             " -.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87"
             " 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95"
             " 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82"
             " .64-.18 1.32-.27 2-.27s1.36.09 2 .27c1.53-1.04 2.2-.82 2.2-.82"
             " .44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75"
             " -3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2"
             " 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8z")}]])

(defn- forkme [site]
  (when-let [repo (or (:github_repo site)
                      (some-> (:github_username site) (str "/daplay")))]
    [:a.forkme
     {:href (str "https://github.com/" repo)
      :rel "noopener noreferrer"
      :target "_blank"
      :aria-label "Fork me on GitHub"}
     [:span.forkme__glow {:aria-hidden "true"}]
     [:span.forkme__band
      [github-mark]
      [:span.forkme__copy
       [:span.forkme__kicker "Open source"]
       [:span.forkme__label "Fork me on GitHub"]]]]))

(defn- nav [{:keys [route]}]
  [:nav.nav {:aria-label "Primary"}
   [brand-mark]
   [:div.nav__links
    [:a {:href "/"
         :class (when (= route "/") "is-active")}
     "Home"]
    [:a {:href "/about/"
         :class (when (= route "/about/") "is-active")}
     "About"]]])

(defn- hero []
  [:header.hero
   [:h1.hero__title "daplay"]
   [:p.hero__lede
    "A personal board of useful links — each tile is Markdown, rendered in ClojureScript."]])

(defn- tile-card [{:keys [title summary url markdown]} index]
  (let [html (md/render markdown)]
    [:article.tile
     {:style {"--i" index}}
     [:a.tile__head {:href url}
      [:h2.tile__title title]
      (when (seq summary)
        [:p.tile__summary summary])]
     (when (seq html)
       [:div.tile__body.prose
        {:dangerouslySetInnerHTML {:__html html}}])]))

(defn home [{:keys [site tiles]}]
  [:div
   [hero]
   [:section.section
    [:div.section__head
     [:h2 "Tiles"]
     [:p (or (:description site)
             "Resource lists drawn from Markdown files.")]]
    [:div.tile-grid
     (for [[i tile] (map-indexed vector tiles)]
       ^{:key (:url tile)}
       [tile-card tile i])]]])

(defn tile-page [{:keys [tile]}]
  (let [html (md/render (:markdown tile))]
    [:article.post
     [:a.post__back {:href "/"} "← Home"]
     [:header.post__header
      [:h1.post__title (:title tile)]
      (when (seq (:summary tile))
        [:p.post__summary (:summary tile)])]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn post-page [{:keys [post]}]
  (let [html (md/render (:markdown post))]
    [:article.post
     [:a.post__back {:href "/"} "← Home"]
     [:header.post__header
      [:time.post__date (format-date (:date post))]
      [:h1.post__title (:title post)]]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn page-view [{:keys [page]}]
  (let [html (md/render (:markdown page))]
    [:article.post
     [:a.post__back {:href "/"} "← Home"]
     [:header.post__header
      [:h1.post__title (:title page)]]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn loading []
  [:div.status "Loading daplay…"])

(defn error-view [message]
  [:div.status.status--error
   [:p "Could not load site data."]
   [:pre message]])

(defn not-found []
  [:div.status
   [:h1 "404"]
   [:p "Nothing at this path."]
   [:a {:href "/"} "Back home"]])

(defn shell [{:keys [route site posts pages tiles status error]}]
  (let [post (some #(when (= (:url %) route) %) posts)
        page (some #(when (= (:url %) route) %) pages)
        tile (some #(when (= (:url %) route) %) tiles)]
    [:div.app
     [:div.app__atmosphere {:aria-hidden "true"}]
     [forkme site]
     [nav {:route route}]
     [:main.app__main
      (case status
        :loading [loading]
        :error   [error-view error]
        (cond
          (= route "/") [home {:site site :tiles tiles}]
          tile          [tile-page {:tile tile}]
          post          [post-page {:post post}]
          page          [page-view {:page page}]
          :else         [not-found]))]
     [:footer.footer
      [:span (:title site)]
      (when-let [gh (:github_username site)]
        [:a {:href (str "https://github.com/" gh)
             :rel "noopener noreferrer"
             :target "_blank"}
         (str "@" gh)])]]))
