(ns {{name}}.lazypp.md-parser
    (:require [clojure.string :as string]
              [instaparse.core :as insta]))

(def ^:private parser
  "Intaparse parser using EBNF."
  (insta/parser
    "<Blocks> = (Paragraph | Header | List | Ordered | Code | Rule)+
    Header = Line Headerline Blankline+
    <Headerline> = h1 | h2
    h1 = '='+
    h2 = '-'+
    List = Listline+ Blankline+
    Listline = Listmarker Whitespace+ Word (Whitespace Word)* EOL
    <Listmarker> = <'+' | '*' | '-'>
    Ordered = Orderedline+ Blankline+
    Orderedline = Orderedmarker Whitespace* Word (Whitespace Word)* EOL
    <Orderedmarker> = <#'[0-9]+\\.'>
    Code = Codeline+ Blankline+
    Codeline = <Space Space Space Space> (Whitespace | Word)* EOL
    Rule = Ruleline Blankline+
    <Ruleline> = <'+'+ | '*'+ | '-'+>
    Paragraph = Line+ Blankline+
    <Blankline> = Whitespace* EOL
    <Line> = Linepre Word (Whitespace Word)* Linepost EOL
    <Linepre> = (Space (Space (Space)? )? )?
    <Linepost> = Space?
    <Whitespace> = #'(\\ | \\t)+'
    <Space> = ' '
    <Word> = #'\\S+'
    <EOL> = <'\\n'> | <#'\\Z'>"))

(defn- wrap-pre-post
  "Receives a tuple of 3 matches (pre-substituion, substitution target, and post-substituion)
  and a transformation function. It returns a tuple in the same structure but with the
  transformed substituion."
  [[pre s post] f]
  [pre (f s) post])

(def ^:private span-elems
  "Array that maps regexes for span elements and functional substituion mechanisms."
  [[#"^(.*?)!\[(.*?)\]\((\S+)\)(.*?)$" (fn [[pre s href post]] [pre [:img {:src href :alt s}] post])]
   [#"^(.*?)\[(.*?)\]\((\S+)\)(.*?)$"  (fn [[pre s href post]] [pre [:a {:href href} s] post])]
   [#"^(.*?)`(.+)`(.*?)$"              #(wrap-pre-post % (fn [s] [:code s]))]
   [#"^(.*?)\*\*(.+)\*\*(.*?)$"        #(wrap-pre-post % (fn [s] [:em s]))]
   [#"^(.*?)__(.+)__(.*?)$"            #(wrap-pre-post % (fn [s] [:em s]))]
   [#"^(.*?)\*(.*?)\*(.*?)$"           #(wrap-pre-post % (fn [s] [:em s]))]
   [#"^(.*?)_(.+)_(.*?)$"              #(wrap-pre-post % (fn [s] [:em s]))]])

(defn- parse-all-possibilities
  "Receives a string and returns a collection of all the span elements that are included in the
  original string."
  [s]
  (map (fn [[regex func]]
         (let [matches (re-matches regex s)]
           (if matches
             (remove empty? (func (drop 1 matches))))))
       span-elems))

(defn- parse-span
  "Receives a string representing a whole span line from the markdown file and returns
  a hiccup version of it that represents the in-line span elements of markup (i.e. *emphasis*
  and `code`)."
  [s]
  (let [all-spans (parse-all-possibilities s)
        match (first (remove nil? all-spans))
        res (if (= (count match) 3)
               (into [(first match) (second match)] (parse-span (last match)))
               match)]
    (if (nil? res)
      [s]
      res)))

(defn- parse-block
  "Receives a particular instaparse block and process it."
  [b]
  (case (first b)
    :List [:ul (for [li (drop 1 b)] [:li (first (parse-span (string/join (drop 2 li))))])]
    :Ordered [:ol (for [li (drop 1 b)] [:li (first (parse-span (string/join (drop 2 li))))])]
    :Header [(first (last b)) (apply str (map parse-span (take (- (count b) 2) (drop 1 b))))]
    :Code [:pre [:code (apply str (interpose "<br />" (for [line (drop 1 b)] (apply str (drop 1 line)))))]]
    :Rule [:hr]
    :Paragraph (into [:p] (parse-span (string/join (drop 1 b))))))

(defn- output-html
  "Receives the parsed blocks from instaparse and processes them."
  [blocks]
  (reduce (fn [accum b]
            (conj accum (parse-block b)))
          [] blocks))

(def markdown-to-hiccup
  "Input is a markdown string and output is a hiccup data structure."
  (comp output-html parser))
