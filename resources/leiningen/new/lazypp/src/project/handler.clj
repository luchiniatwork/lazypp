(ns {{name}}.handler
    (:require [compojure.core :refer [defroutes GET ANY]]
              [compojure.route :as route]
              [{{name}}.contents :as contents]
              [{{name}}.layout :as layout]
              [{{name}}.lazypp.parser :as parser]
              [{{name}}.lazypp.watcher :as watcher]
              [hiccup.page :as hiccup]))

(defroutes routes
  (GET "/" [] (apply layout/application {:include-js hiccup/include-js
                                         :include-css hiccup/include-css}
                     (parser/parse-slides (contents/index))))
  (route/resources "/" {:root "."})
  (route/not-found (layout/not-found)))

(def app routes)

(watcher/start)
