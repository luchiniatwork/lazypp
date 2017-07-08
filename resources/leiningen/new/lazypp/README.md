# {{name}}

This is a presentation for {{name}}.

## Usage

1. Run `$ lein ring server-headless`
2. Point your browser to (http://localhost:3000)
3. Change file `src/{{sanitized}}/contents.clj`
4. Add any assets you need to `resources`
5. See your changes reflected on your browser as you save them
6. Your presentation is also generated as a static html on the project directory

## Editing

The file `src/{{sanitized}}/contents.clj` contains the presentation content. To learn more
about how to use `lazypp` refer to the [docs](https://github.com/luchiniatwork/lazypp).

## Presenting

While on your browser, you can: 

* press `j` to jump to a slide
* press `p` to see the print view
* press `t` to go back to the talk view

## Options

You can choose a `light` or a `dark` theme. Simply change the following line on file
`src/{{sanitized}}/layout.clj` accordingly:

```clojure
[:body {:class "light"} content]
```

## License

Copyright © 2017 FIXME

FIXME
