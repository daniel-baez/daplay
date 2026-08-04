(ns daplay.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [daplay.data :as data]
            [daplay.views :as views]))

(defonce !state
  (r/atom {:status :loading
           :route  "/"
           :site   nil
           :posts  []
           :pages  []
           :error  nil}))

(defonce !root (atom nil))

(defn- boot-config []
  (or (js->clj (.-__DAPLAY__ js/window) :keywordize-keys true)
      {:route "/"
       :dataUrl "/assets/data/site.json"}))

(defn mount-root! []
  (let [el (.getElementById js/document "app")]
    (when el
      (when-not @!root
        (reset! !root (rdom/create-root el)))
      (rdom/render @!root [views/shell @!state]))))

(defn- load-data! []
  (let [{:keys [dataUrl route]} (boot-config)]
    (swap! !state assoc :route (or route "/") :status :loading)
    (-> (data/fetch-site! dataUrl)
        (.then (fn [[tag payload]]
                 (if (= tag :ok)
                   (swap! !state assoc
                          :status :ready
                          :site   (:site payload)
                          :posts  (vec (:posts payload))
                          :pages  (vec (:pages payload))
                          :error  nil)
                   (swap! !state assoc :status :error :error payload)))))))

(defn init! []
  (load-data!)
  (mount-root!)
  (add-watch !state :render (fn [_ _ _ _] (mount-root!))))
