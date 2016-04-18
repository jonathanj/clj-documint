(defproject documint "0.1.0-SNAPSHOT"
  :description "Document processing service"
  :url "http://github.com/fusionapp/clj-documint"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.danielsz/system "0.2.0"]
                 [org.xhtmlrenderer/flying-saucer-pdf-itext5 "9.0.8"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-core "1.4.0"]
                 [danlentz/clj-uuid "0.1.6"]
                 [aleph "0.4.1-beta2"]
                 [liberator "0.14.0"]
                 [bidi "1.25.0"]
                 [compojure "1.4.0"]
                 [environ "1.0.2"]
                 [manifold "0.1.2"]
                 [org.apache.pdfbox/pdfbox "2.0.0-RC3"]
                 [org.bouncycastle/bcpkix-jdk15on "1.54"]
                 [org.bouncycastle/bcprov-jdk15on "1.54"]
                 [prismatic/schema "1.0.4"]
                 [com.twelvemonkeys.imageio/imageio-tiff "3.2.1"]
                 [com.twelvemonkeys.imageio/imageio-metadata "3.2.1"]
                 [com.levigo.jbig2/levigo-jbig2-imageio "1.6.5"]
                 ]
  :plugins [[lein-environ "1.0.0"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :main ^:skip-aot documint.core
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["dev"]
                   :env {:documint-port 3000}}
             :prod {:env {:documint-port 3000}}
             :uberjar {:aot :all}
             :drone {:local-repo "m2"}})
