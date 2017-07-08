(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-future-spec "1.9.0-alpha14"]
                 [hiccup "1.0.5"]
                 [instaparse "1.4.7"]
                 [compojure "1.5.2"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [watchtower "0.1.1"]
                 [org.clojure/data.codec "0.1.0"]]
  :target-path "target/%s"
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies []}}
  :plugins [[lein-ring "0.11.0"]]
  :ring {:handler {{name}}.handler/app
         :auto-reload? true
         :auto-refresh? true
         :reload-paths ["src" "resources"]
         :port 3000})
