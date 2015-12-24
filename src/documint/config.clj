(ns documint.config
  "Documint configuration files."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as jio]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [documint.schema :refer [path-exists?]]))


(def ^:private config-schema
  ""
  {:keystore {:path     path-exists?
              :password s/Str}
   :signing  {:certificate-passwords {s/Keyword s/Str}}
   :renderer {(s/optional-key :font-path) path-exists?}})


(defn- user-home
  "Get the system property for the user's home directory."
  []
  (System/getProperty "user.home"))


(defn- run-dir
  "Get the system property for the directory the application was invoked from."
  []
  (System/getProperty "user.dir"))


(defn parse-config
  "Parse a config file as JSON."
  [file]
  (if (.exists file)
    (do
      (log/info "Reading config file"
                file)
      (json/read (jio/reader file) :key-fn keyword))
    (do
      (log/info "Skipping nonexistent config file"
                file)
      {})))


(defn- known-config-paths
  "Build a vector of known config file paths."
  []
  (vector (jio/file (user-home) ".config" "documint" "config.json")
          (jio/file (run-dir) "documint.config.json")))


(defn validate-config
  "Validate the configuration data."
  [config]
  (s/validate config-schema config))


(defn load-config
  "Load config files from all known paths if they exist and combine them."
  ([]
   (load-config (known-config-paths) {}))

  ([known-paths default-config]
   (log/info "Loading configuration")
   (->> known-paths
        (reduce (fn [config f]
                  (merge config (parse-config f)))
                default-config)
        validate-config)))
