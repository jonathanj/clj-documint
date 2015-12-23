(set-env!
 :resource-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.nrepl "0.2.11"]
                 [org.danielsz/system "0.2.0"]
                 [org.xhtmlrenderer/flying-saucer-pdf-itext5 "9.0.8"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-core "1.4.0"]
                 [danlentz/clj-uuid "0.1.6"]
                 [aleph "0.4.1-beta2"]
                 [liberator "0.13"]
                 [bidi "1.21.1"]
                 [compojure "1.4.0"]
                 [environ "1.0.1"]
                 [danielsz/boot-environ "0.0.5"]
                 [manifold "0.1.1"]
                 [org.apache.pdfbox/pdfbox "2.0.0-RC2"]
                 [org.bouncycastle/bcpkix-jdk15on "1.47"]
                 [org.bouncycastle/bcprov-jdk15on "1.47"]
                 ])


(require
 '[reloaded.repl :as repl :refer [start stop go reset]]
 '[documint.systems :refer [dev-system]]
 '[danielsz.boot-environ :refer [environ]]
 '[system.boot :refer [system run]])


(deftask dev
  "Run a restartable system in the Repl"
  []
  (comp
   (environ :env {:documint-port 3000})
   (watch :verbose true)
   (system :sys #'dev-system :auto-start true :hot-reload true)
   (repl :server true)))


(deftask dev-run
  "Run a dev system from the command line"
  []
  (comp
   (environ :env {:documint-port 3000})
   (run :main-namespace "documint.core" :arguments [#'dev-system])
   (wait)))


(deftask build
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace '#{documint.core})
   (pom :project 'myproject
        :version "1.0.0")
   (uber)
   (jar :main 'documint.core)))
