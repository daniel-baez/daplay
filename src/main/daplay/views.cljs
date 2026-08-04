(ns daplay.views
  (:require [daplay.markdown :as md]))

(defn- format-date [iso]
  (when iso
    (let [d (js/Date. iso)]
      (.toLocaleDateString
       "en-GB"
       d
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
     "Journal"]
    [:a {:href "/about/"
         :class (when (= route "/about/") "is-active")}
     "About"]]])

(defn- hero []
  [:header.hero
   [:p.hero__eyebrow "daplay.cl"]
   [:h1.hero__title "daplay"]
   [:p.hero__lede
    "Markdown lives in Jekyll. The interface is ClojureScript — fast, expressive, and built for play."]])

(defn- post-card [{:keys [title url date excerpt]} index]
  [:a.post-card
   {:href url
    :style {"--i" index}}
   [:time.post-card__date (format-date date)]
   [:h2.post-card__title title]
   (when (seq excerpt)
     [:p.post-card__excerpt excerpt])])

(defn home [{:keys [site posts]}]
  [:div
   [hero]
   [:section.section
    [:div.section__head
     [:h2 "Journal"]
     [:p (or (:description site)
             "Notes rendered from raw Markdown via ClojureScript.")]]
    [:div.post-list
     (for [[i post] (map-indexed vector posts)]
       ^{:key (:url post)}
       [post-card post i])]]])

(defn post-page [{:keys [post]}]
  (let [html (md/render (:markdown post))]
    [:article.post
     [:a.post__back {:href "/"} "← Journal"]
     [:header.post__header
      [:time.post__date (format-date (:date post))]
      [:h1.post__title (:title post)]]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn page-view [{:keys [page]}]
  (let [html (md/render (:markdown page))]
    [:article.post
     [:a.post__back {:href "/"} "← Journal"]
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

(defn shell [{:keys [route site posts pages status error]}]
  (let [post (some #(when (= (:url %) route) %) posts)
        page (some #(when (= (:url %) route) %) pages)]
    [:div.app
     [:div.app__atmosphere {:aria-hidden "true"}]
     [nav {:route route}]
     [:main.app__main
      (case status
        :loading [loading]
        :error   [error-view error]
        (cond
          (= route "/") [home {:site site :posts posts}]
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
