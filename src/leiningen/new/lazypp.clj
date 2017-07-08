(ns leiningen.new.lazypp
  (:require [clojure.string :as str]
            [leiningen.new.templates :refer [renderer raw-resourcer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "lazypp"))

(def raw (raw-resourcer "lazypp"))

(def templates [".gitignore"
                "project.clj"
                "README.md"
                "src/{{sanitized}}/contents.clj"
                "src/{{sanitized}}/handler.clj"
                "src/{{sanitized}}/layout.clj"
                "src/{{sanitized}}/lazypp/md_parser.clj"
                "src/{{sanitized}}/lazypp/parser.clj"
                "src/{{sanitized}}/lazypp/watcher.clj"])

(def raw-files ["resources/js/big.js"
                "resources/js/highlight.pack.js"
                "resources/css/big.css"
                "resources/css/highlight.css"
                "resources/css/fonts/rubik-v4-latin-regular.woff"
                "resources/css/fonts/rubik-v4-latin-regular.woff2"])

(defn render-data
  "Replaces {{sanitized}} with the string project and renders data into it."
  [template-path data]
  (render (-> template-path
              (str/replace #"\{\{sanitized\}\}" "project"))
          data))

(defn lazypp
  "Generates a lazypp template."
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' lazypp project.")
    (let [rendered-set (map (fn [x] [x (render-data x data)]) templates)
          raw-set (map (fn [x] [x (raw x)]) raw-files)]
      (apply (partial ->files data) (concat rendered-set raw-set)))))
