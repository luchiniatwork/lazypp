(ns {{name}}.lazypp.parser
    (:require [clojure.java.io :as io]
              [{{name}}.lazypp.md-parser :as md]))

(defn- slide-param
  "Transforms slide-specific helper options into hiccup parameters."
  [k v]
  (cond
    (= k :timed) [:data-time-to-next (str v)]
    (= k :image) [:data-background-image v]
    (= k :nobreak) [:class "nobreak"]))

(defn- code-param
  "Transforms code-specific helper function options into hiccupe parameters."
  [k v]
  (cond
    (= k :type) [:class v]))

(defn- params-selector
  "Helper function to select the parameters for hiccup nodes."
  [opt condf]
  (if-not (nil? opt)
    (reduce-kv (fn [a k v]
                 (let [[nk nv] (condf k v)]
                   (when-not (nil? nk)
                     (assoc a nk nv)))) {} opt)))

(defn- slide-params
  "Transforms a set of slide-specific options into hiccup parameters"
  [opt]
  (params-selector opt slide-param))

(defn- code-params
  "Transforms a set of code-specific options into hiccup parameters"
  [opt]
  (params-selector opt code-param))

(defn- slide-nodes
  "Similarly to slide-params, some slide-specific options generate hiccup sub-nodes instead"
  [opt]
  (if (and (map? opt) (contains? opt :notes))
    (let [notes (:notes opt)
          normalized (if (string? notes) [notes] notes)]
      (mapv (fn [x] [:notes x]) normalized))))

(defn- not-nil? [x]
  (not (nil? x)))

(defn- route
  "Simple reducer to allow for markdown transformations in case of strings."
  [a x]
  (if (string? x)
    (reduce #(conj %1 %2) a (md/markdown-to-hiccup x))
    (conj a x)))

(defn- root-level-slide
  "All root-level helper functions share this piece of code so that they can be
  used interchangeably."
  [reduce-func]
  (fn [opt & c]
    (let [rc (if (and (map? opt) (contains? opt :file))
               [(slurp (io/resource (:file opt)))]
               c)]
      (if-not (or (map? opt) (nil? opt))
        (filterv not-nil? (reduce-func nil (remove nil? (conj [] opt rc))))
        (filterv not-nil? (reduce-func opt rc))))))

(def big
  "Generates a simple slide where the text is as big as possible.

  The first (optional) argument can be a map with:

  * `:nobreak` - boolean: if `true` then the content is printed in one line
    without a break (default `false`)
  * `:timed` - integer: number of seconds for automatically skipping to the next
    slide
  * `:image` - string: an image relative to `./resources` to be used as background
    to this slide
  * `:notes` - string or collection: speaker notes for the specific slide
  * `:file` - string: a markdown file relative to `./resources` to e used as the
    content of this slide.

  The rest and subsequent parameters represent any sub nodes for the slide."
  (root-level-slide
          (fn [opt c]
            (reduce route (into [:div (slide-params opt)] (slide-nodes opt)) c))))

(def slide
  "Alias to `big`. See `big` for more details"
  big)

(def small
  "Generates a simple slide where the text is as small as possible - particularly
  in relation to other big text in the same slide.

  This helper shares the same high-level options and signature as `big`.

  See `big` for details on usage."
  (root-level-slide
            (fn [opt c]
              [:div (slide-params opt) (reduce route [:small] c)])))

(def items
  "Generates a simple slide where ech item of the array sent as a paramter is an item
  in a list.
  
  This helper shares the same high-level options and signature as `big`.

  See `big` for details on usage."
  (root-level-slide
            (fn [opt c]
              [:div (map #(big {:nobreak true} %) (first c))])))

(def code
  "Generates a simple slide where the content is a code extract. The system will try
  to auto-identify the code for highlights but, in case you need to specify one directly
  simply send `:type` and a type id as part of the first parameter.

  This helper shares the same high-level options and signature as `big`.

  See `big` for details on usage. In addition, this helper also supports a field called
  `:type` to help the highlighter behave better. See https://highlightjs.org/ for more
  details."
  (root-level-slide
           (fn [opt c]
             [:div (slide-params opt) [:pre [:code (code-params opt) (first c)]]])))

(defn parse-slides
  "Makes sure that a base slide-set is in proper format - particularly enforcing string
  entries to be wrapped in `slide` helper calls."
  [slide-set]
  (map #(if (string? %) (slide %) %)
       slide-set))
