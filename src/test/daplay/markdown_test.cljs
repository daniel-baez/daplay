(ns daplay.markdown-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.string :as str]
            [daplay.markdown :as md]))

(deftest render-nil-or-non-string
  (is (nil? (md/render nil)))
  (is (nil? (md/render 42))))

(deftest render-basic-markdown
  (let [html (md/render "# Hello\n\nSome **bold** text.")]
    (is (str/includes? html "<h1>"))
    (is (str/includes? html "Hello"))
    (is (str/includes? html "<strong>bold</strong>"))))

(deftest external-links-open-in-new-tab
  (testing "http(s) links get target and rel"
    (let [html (md/render "[Example](https://example.com/path)")]
      (is (str/includes? html "href=\"https://example.com/path\""))
      (is (str/includes? html "target=\"_blank\""))
      (is (str/includes? html "rel=\"noopener noreferrer\""))))
  (testing "relative links stay in-page"
    (let [html (md/render "[About](/about/)")]
      (is (str/includes? html "href=\"/about/\""))
      (is (not (str/includes? html "target=\"_blank\""))))))

(deftest raw-html-is-not-passed-through
  (let [html (md/render "<script>alert(1)</script>")]
    (is (not (str/includes? html "<script>")))))
