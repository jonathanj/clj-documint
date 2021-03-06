(ns documint.render.flying-saucer
  "Flying Saucer `IRenderer` implementation.

  See <https://github.com/flyingsaucerproject/flyingsaucer>."
  (:require [clojure.tools.logging :as log]
            [documint.render :refer [IRenderer]])
  (:import [java.io InputStream OutputStream]
           [org.w3c.dom Document]
           [org.xhtmlrenderer.pdf ITextRenderer]
           [org.xhtmlrenderer.resource XMLResource]
           [org.xhtmlrenderer.util XRLog]))


(defn- find-base-uri
  "Determine the `href` value of the first `base` tag."
  [^Document document]
  (some-> (.getElementsByTagName document "base")
          (.item 0)
          (.getAttributes)
          (.getNamedItem "href")
          (.getTextContent)))


(defrecord FlyingSaucerRenderer [^ITextRenderer renderer]
  IRenderer
  (render [this input output
           {:keys [base-uri] :as options}]
    (let [^InputStream input input
          document           (.getDocument (XMLResource/load input))
          base-uri           (if (clojure.string/blank? base-uri)
                               (find-base-uri document)
                               base-uri)]
      (log/info "Rendering a document with Flying Saucer"
                {:base-uri base-uri})
      (log/spy
       (doto renderer
         (.setDocument document base-uri)
         (.layout)
         (.createPDF output))))))


(defn- set-logging
  "Enabled / disable Flying Saucer logging."
  [enabled?]
  (if enabled?
    (System/setProperty "xr.util-logging.loggingEnabled" "true")
    (System/clearProperty "xr.util-logging.loggingEnabled"))
  (XRLog/setLoggingEnabled (boolean enabled?)))


(defn renderer
  "Construct a Flying Saucer `IRenderer` implementation.

  Recognised options:

  `font-path`: Path string to additional font directories."
  [{:keys [font-path
           logging?]
    :as options}]
  (log/info "Initialising Flying Saucer renderer"
            {:options options})
  (set-logging logging?)
  (let [renderer (ITextRenderer.)]
    (when font-path
      (.addFontDirectory (.getFontResolver renderer) font-path true))
    (.. renderer
        (getSharedContext)
        (getTextRenderer)
        (setSmoothingThreshold -1))
    (map->FlyingSaucerRenderer {:renderer renderer})))
