(ns {{name}}.contents
    (:require [{{name}}.lazypp.parser :refer :all]))

(defn index
  "Here's where your slide-set is created. Return an array mixing strings, and
  hiccup data points.

  The following helper functions are available: `slide`, `big`, `small`, `items`,
  and `code`. All of them can be used at the root level of the slide or composed
  to create more advanced formats.

  See `parser.clj` for more details on the helper functions.

  Simple strings are simply wrapped in a `slide` call."
  []
  ["Slide 1"
   (slide "Slide 2")
   "Slide with *emphasis*"
   "Another way to have **emphasis**"
   "Or maybe _this way_"
   "Yet __another way__"
   "With some `code`"
   (big "Big-font slide")
   (small "Small-font slide - kinda 🙈")
   (small {:nobreak true} "Small-font slide - kinda 🙈")
   (big "Did I mention *emojis* via unicode?"
        (big "🙅🙌"))
   (big {:nobreak true} "Big-font slide (`nobreak`)")
   (slide {:timed 3 :nobreak true} "Wait 3s...")
   (slide (small "Combining _Small_...")
          (big "...and *big*"))
   "Inline `code`? *Doable*."
   (code "(- 1 2)")
   (slide {:notes "One note"} "This slide has _one_ note")
   (slide {:notes ["Note 1" "Note 2"]} "This slide has _TWO_ notes")
   "The *end*!"])
