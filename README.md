# LazyPP

The PowerPoint/Keynote of the Lazy Pragmatic Presenter.

## What is LazyPP?

LazyPP is a simple lein template for Clojure that allows anyone with minimal programming skills
to put a relatively pretty yet simple presentation using just code.

## Motivation

We all need to create presentations every now and again. You might be the best Keynote or PowerPoint
operator in the universe and you'll still have to worry about making it "look right." There will be
alignment issues, font choice, color choice, grids and all sorts of details that take you away from
what really matters: *the message* your are trying too deliver.

Wouldn't it be better to just concentrate on *the message* and let the rest sort itself out to you?

I also find it really annoying to have to leave my *development environment and practices behind* every
time I need to create presentations. For me, writing code is a form of telling a message, if I have
all these tools to tell a message, why am I bothering with other tools?

Wouldn't it be great if I could prepare presentations from the *comfort of my development environment*?

## Before you start

Make sure you have the minimum requirements installed on your system:

1. Java SE Runtime Environment (JRE) 1.8.x (https://www.java.com/en/download/help/download_options.xml)
2. Leiningen (https://leiningen.org/)

## Getting started

First run lein to create an app for your presentation:

    $ lein new lazypp my_presentation

You can name `my_presentation` anyway you prefer of course.

Then run the presentation by:

    $ cd my_presentation/
    $ lein ring server-headless

A `ring` server will be started attached to port `3000`. Connect to it by pointing your browser to:

* [http://localhost:3000](http://localhost:3000)

From now on, any changes you make to `src/my_presentation/contents.clj` will be immediatelly reflected
on your browser window.

Check the [detailed docs](#documentation) to learn which helper functions are available when creating
content.

Check [presenting tips](#presenting) on how to operate the browser in presentation mode.

## Offline mode

Your presentation is also generated as a static and fully independent HTML on the project directory
called `my_presentation.html`. This file can be used and shared without an HTTP server process.

## Presenting

While on your browser, you can: 

* press `j` to jump to a slide
* press `p` to see the print view
* press `t` to go back to the talk view

## Themes

You can choose a `light` or a `dark` theme. Simply change the following line on file
`src/{{sanitized}}/layout.clj` accordingly:

```clojure
[:body {:class "light"} content]
```

## Documentation

The core principle behind LazyPP is that a vector of simple nested structures will be
parsed into a presentation.

The basic template creates a file on `src/<presentation_name>/contents.clj` with a single
function `index`. This function is supposed to return said vector forming therefore, the
whole presentation..

Here are the structures you can return on this vector.

### 1. Simple string

A string will render as the slide's content. Example:

```clojure
["This is the content of the slide."]
```

### 2. Simple slide wrappers

There are three simple slide wrapping functions: `slide`, `big` and `small`. Both `slide` and `big`
are fully equivalent and render the content in big fonts. Conversely, `small` will try to have
a relatively smaller rendering of the content. Example:

```clojure
[(slide "Slide content")
 (big "Also a slide content")
 (small "Smaller slide content")]
```

### 3. Timed slides

You might want a slide to flip to the next one automatically after some time. Simply use any one of
the wrappers with the `:timed` parameter. Example:

```clojure
[(slide {:timed 10} "Will turn after 10s...")]
```

### 4. Forcing text to one line

LazyPP prefers breaking text into multiple lines. In order to force it to keep the line without
breaks, ise the `:nobreak` parameter. Example:

```clojure
[(slide {:nobreak true} "This will be rendered in one line.")]
```

### 5. Markdown formatting

LazyPP supports a limited subset of markdown markup. You should be able to accomplish things like:

```clojure
["Slide with *emphasis*"
 "Another way to have **emphasis**"
 "Or maybe _this way_"
 "Yet __another way__"
 "With some `code`"
 "![](images/pic.jpg)"
 "Link [here](https://www.google.com)"]
```

### 6. Slides for code

Code is a core piece of some presentations. Code can be inlined in markdown are with highlights
on a full slide:

```clojure
[(code "(+ 1 2 3 4 5)")]
```

By leveraging `highlight.js` you can hint which language is in the slide and have better coloring:

```clojure
[(code {:type :clojure} "(+ 1 2 3 4 5)")]
```

### 7. Content from files

You can drop your assets in your `resources` directory and consume them in your presentation.
If these assets are markdown files or code files you can inject them as content to your slides
as shown below:

The Clojure file `resources/example.clj` will be the content of the slide:

```clojure
[(code {:file "example.clj"})]
```

The Markdown file `resources/example.md` will be the content of the slide:

```clojure
[(slide {:file "example.md"})]
```

### 8. Background images

Images can be added to the background. They are sourced from the `resources` directory.
The following adds `resources/background.jpg` to the slide's background.

```clojure
[(slide {:image "background.jpg"})]
```

### 9. Items' slides

Slides can be organized in lists. The wrapper function that helps this is `items`:

```clojure
[(items ["Item 1" "Item 2" "Item 3"])]
```

### 10. Slide notes

Presenter notes are key to some presentations. You can add them by using the `:notes`
parameter:

```clojure
[(slide {:notes "One note"} "This slide has _one_ note")
 (slide {:notes ["Note 1" "Note 2"]} "This slide has _TWO_ notes")]
```

## Thanks

* [Big](https://github.com/tmcw/big) which lends most of the JS/CSS work to LazyPP
* [highlight.js](https://highlightjs.org/) for the beautiful code highlighting

## License

Copyright (c) 2017 Tiago Luvhini

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
