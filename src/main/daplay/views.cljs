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
