(ns {{name}}.layout
    (:require [hiccup.page :refer [html5]]))

(defn application
  "Generates the HTML out of hiccup data points using hiccup's `html5` macro.

  The first argument is a map to two functions `include-js` and `include-css`.
  Traditoonally these two are injected as from the package `hiccup.page`. The
  injection here makes it possible for consumers to give different behaviors to
  JS and CSS processing (i.e. copying the whole file for offline purposes

  The second and subsequent arguments are the hiccup data points to be used as
  `body` content of the webpage."
  [{:keys [include-js include-css]} & content]
  (html5 [:head
          [:title "{{name}}"]
          [:meta {:charset "utf-8"}]
          [:meta
           {:name "viewport",
            :content
            "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"}]
          (include-css "css/big.css")
          (include-css "css/highlight.css")
          (include-js "js/big.js")
          (include-js "js/highlight.pack.js")
          [:script "hljs.initHighlightingOnLoad();"]]
         [:body {:class "light"} content]))

(defn not-found
  "Generates the HTML for 404 cases."
  []
  (html5 [:body "404 - Not Found"]))
