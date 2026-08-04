(ns daplay.views
  (:require [clojure.string :as str]
            [daplay.markdown :as md]))

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

(defn- social-icon [view-box path]
  [:svg.footer__icon
   {:viewBox view-box
    :aria-hidden "true"
    :focusable "false"}
   [:path {:fill "currentColor" :d path}]])

(defn- linkedin-icon []
  [social-icon "0 0 24 24"
   (str "M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136"
        " 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37"
        " -1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063"
        " -.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064"
        " 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452z"
        "M22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451"
        "C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z")])

(defn- twitter-icon []
  [social-icon "0 0 24 24"
   (str "M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99"
        " 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833"
        "L7.084 4.126H5.117z")])

(defn- github-icon []
  [social-icon "0 0 24 24"
   (str "M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113"
        " .82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61"
        " -4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084"
        " -.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998"
        " .108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465"
        " -2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23"
        " .96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285"
        " -1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22"
        " 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896"
        " -.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0"
        " -6.627-5.373-12-12-12")])

(defn- social-link [{:keys [href label icon]}]
  [:a.footer__social
   {:href href
    :rel "noopener noreferrer"
    :target "_blank"
    :aria-label label}
   icon
   [:span.footer__sr-only label]])

(defn- footer-socials [site]
  (let [links (cond-> []
                (:linkedin_username site)
                (conj {:href (str "https://linkedin.com/in/" (:linkedin_username site))
                       :label "LinkedIn"
                       :icon [linkedin-icon]})
                (:twitter_username site)
                (conj {:href (str "https://x.com/" (:twitter_username site))
                       :label "Twitter"
                       :icon [twitter-icon]})
                (:github_username site)
                (conj {:href (str "https://github.com/" (:github_username site))
                       :label "GitHub"
                       :icon [github-icon]}))]
    (when (seq links)
      [:nav.footer__socials {:aria-label "Social"}
       (for [link links]
         ^{:key (:label link)}
         [social-link link])])))

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
    [:a {:href "/tiles/"
         :class (when (str/starts-with? (or route "") "/tiles/")
                  "is-active")}
     "Tiles"]
    [:a {:href "/"
         :class (when (= route "/") "is-active")}
     "Blog"]
    [:a {:href "/about/"
         :class (when (= route "/about/") "is-active")}
     "About"]]])

(defn- montaigne-epigraph
  "Opening epigraph from Montaigne's Essais — signals a personal site."
  []
  [:figure.montaigne
   [:p.montaigne__eyebrow "Au lecteur"]
   [:blockquote.montaigne__quote {:cite "https://fr.wikisource.org/wiki/Essais/Adresse_au_lecteur"
                                  :lang "fr"}
    [:span.montaigne__mark {:aria-hidden "true"} "«"]
    [:p.montaigne__fr
     [:span.montaigne__fr-lead "Ainsi, lecteur, je suis moi-même la matière de mon livre"]
     [:span.montaigne__fr-rest
      " : ce n’est pas raison que tu emploies ton loisir en un sujet si frivole et si vain."]]]
   [:figcaption.montaigne__caption
    [:p.montaigne__en {:lang "en"}
     "Thus, reader, I myself am the matter of my book — it is not reasonable that you spend your leisure on so frivolous and vain a subject."]
    [:cite.montaigne__cite "Michel de Montaigne · Essais"]]])

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

(defn- post-item [{:keys [title url date excerpt]} index]
  [:article.post-item
   {:style {"--i" index}}
   [:a.post-item__link {:href url}
    [:time.post-item__date (format-date date)]
    [:h2.post-item__title title]
    (when (seq excerpt)
      [:p.post-item__excerpt excerpt])]])

(defn- empty-posts []
  [:div.empty
   [:p.empty__title "No posts yet"]
   [:p.empty__body
    "New notes will show up here first. Until then, the tiles have curated links."]
   [:a.empty__cta {:href "/tiles/"} "Browse tiles"]])

(defn home [{:keys [posts]}]
  [:div
   [:header.hero
    [montaigne-epigraph]]
   [:section.section
    (if (seq posts)
      [:div.post-list
       (for [[i post] (map-indexed vector posts)]
         ^{:key (:url post)}
         [post-item post i])]
      [empty-posts])]])

(defn tiles-board [{:keys [tiles]}]
  [:div
   [:header.page-hero
    [:h1.page-hero__title "Tiles"]
    [:p.page-hero__lede
     "Curated lists of useful links I keep coming back to."]]
   [:section.section
    [:div.tile-grid
     (for [[i tile] (map-indexed vector tiles)]
       ^{:key (:url tile)}
       [tile-card tile i])]]])

(defn tile-page [{:keys [tile]}]
  (let [html (md/render (:markdown tile))]
    [:article.post
     [:a.post__back {:href "/tiles/"} "← Tiles"]
     [:header.post__header
      [:h1.post__title (:title tile)]
      (when (seq (:summary tile))
        [:p.post__summary (:summary tile)])]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn post-page [{:keys [post]}]
  (let [html (md/render (:markdown post))]
    [:article.post
     [:a.post__back {:href "/"} "← Blog"]
     [:header.post__header
      [:time.post__date (format-date (:date post))]
      [:h1.post__title (:title post)]]
     [:div.prose
      {:dangerouslySetInnerHTML {:__html html}}]]))

(defn page-view [{:keys [page]}]
  (let [html (md/render (:markdown page))]
    [:article.post
     [:a.post__back {:href "/"} "← Blog"]
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
   [:a {:href "/"} "Back to blog"]])

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
          (= route "/")       [home {:posts posts}]
          (= route "/tiles/") [tiles-board {:tiles tiles}]
          tile                [tile-page {:tile tile}]
          post                [post-page {:post post}]
          page                [page-view {:page page}]
          :else               [not-found]))]
     [:footer.footer
      [:p.footer__copy "daplay.cl · all rights reserved"]
      [footer-socials site]]]))
