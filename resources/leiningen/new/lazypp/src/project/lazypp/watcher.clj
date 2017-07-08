(ns {{name}}.lazypp.watcher
    (:require [clojure.java.io :as io]
              [clojure.string :as string]
              [clojure.data.codec.base64 :as b64]
              [{{name}}.contents :as contents]
              [{{name}}.layout :as layout]
              [{{name}}.lazypp.parser :as parser]
              [watchtower.core :as watcher]))

(defn- include-js
  "Includes the JS file specfied in `f` as relative to `./resources` directly into the
  target html.
  
  Makes sure any `script` tags are properly sanitized."
  [f]
  (let [c (slurp (io/resource f))]
    (str "<script type=\"text/javascript\">"
         (-> c
             (string/replace #"<script" "<scr\" + \"ipt")
             (string/replace #"</script" "</scr\" + \"ipt"))
         "</script>")))

(defn- include-css
  "Includes the CSS file specified in `f` as relative to `./resources` directly into the
  target html."
  [f]
  (let [c (slurp (io/resource f))]
    (str "<style>"
         c
         "</style>")))

(defn- encode-image
  "Encodes the image specified in `f` as relative to `./resources` as a base 64 format."
  [f]
  (with-open [in (-> f
                     io/resource
                     io/input-stream)
              out (new java.io.ByteArrayOutputStream)]
    (b64/encoding-transfer in out)
    (.toString out)))

(defn- collect-image-files
  "Receies a regex and a string representing the content of the html file. The regex must
  have one and only one group that will extract the name of the file to be injected.

  Returns an array with all the names that match the regex."
  [regex c]
  (let [matcher (re-matcher regex c)]
    (loop [match (re-find matcher)
           result []]
      (if-not match
        result
        (recur (re-find matcher)
               (conj result (last match)))))))

(defn- embed-images
  "Receives a string representation of the html and returns it with the images embedded in
  base 64 format.

  It searches for images in `img` tags as well as `data-background-image` attributes."
  [c]
  (let [target (atom c)]
    (doseq [f (collect-image-files #"<img .*? src=\"(.*?)\"" c)]
      (do
        (let [regex (re-pattern (str "src=\\\"" f "\\\""))
              encoded (encode-image f)]
          (reset! target
                  (string/replace @target regex
                                  (str "src=\"" "data:;base64,"
                                       encoded "\""))))))
    (doseq [f (collect-image-files #"data-background-image=\"(.*?)\"" c)]
      (do
        (let [regex (re-pattern (str "data-background-image=\\\"" f "\\\""))
              encoded (encode-image f)]
          (reset! target
                  (string/replace @target regex
                                  (str "data-background-image=\""
                                       "data:;base64," encoded "\""))))))
    @target))

(defn- on-change
  "Triggers a regeneration of the offline file."
  [changed-files]
  (println "Updated" "{{name}}.html")
  (spit
   "{{name}}.html"
   (-> (apply layout/application {:include-js include-js
                                  :include-css include-css}
              (parser/parse-slides (contents/index)))
       embed-images)))

(defn start
  "Starts the watcher for when files change."
  []
  (watcher/watcher ["src/" "resources/"]
                   (watcher/rate 200)
                   (watcher/on-change on-change)))
