(defproject buddy "0.3.0-SNAPSHOT"
  :description "Security library for Clojure"
  :url "https://github.com/niwibe/buddy"
  :license {:name "BSD (2-Clause)"
            :url "http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/algo.monads "0.1.5"]
                 [ring/ring-core "1.3.2" :exclusions [org.clojure/tools.reader]]
                 [commons-codec/commons-codec "1.10"]
                 [cats "0.2.0" :exclusions [org.clojure/clojure]]
                 [clout "2.1.0"]
                 [com.taoensso/nippy "2.7.1"]
                 [clojurewerkz/scrypt "1.2.0"]
                 [clj-time "0.8.0"]
                 [swiss-arrows "1.0.0"]
                 [cheshire "5.4.0"]
                 [org.bouncycastle/bcprov-jdk15on "1.51"]
                 [org.bouncycastle/bcpkix-jdk15on "1.51"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :test-paths ["test"]
  :plugins [[codox "0.8.10"]
            [lein-cloverage "1.0.2"]]
  :codox {:output-dir "doc/api"
          :src-dir-uri "http://github.com/niwibe/buddy/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :profiles {:bench {:dependencies [[criterium "0.4.3"]]
                     :source-paths ["benchmarks/"]
                     :main ^:skip-aot buddy.benchmarks}
             ;; :dev {:dependencies [[speclj "3.1.0"]]
             ;;       :test-paths ["spec"]
             ;;       :plugins [[speclj "3.1.0"]]}
             :speclj {:dependencies [[speclj "3.1.0"]]
                      :test-paths ["spec"]
                      :plugins [[speclj "3.1.0"]]}
             :example {:dependencies [[compojure "1.1.8"]
                                      [ring "1.3.1"]]}
             :sessionexample [:example
                              {:source-paths ["examples/sessionexample/src"]
                               :resource-paths ["examples/sessionexample/resources"]
                               :target-path "examples/sessionexample/target/%s"
                               :main ^:skip-aot sessionexample.core}]
             :oauthexample [:example
                            {:dependencies [[clj-http "0.7.9"]
                                            [hiccup "1.0.5"]
                                            [org.clojure/data.json "0.2.4"]]
                             :source-paths ["examples/oauthexample/src"]
                             :resource-paths ["example/oauthexample/resources"]
                             :target-path "examples/oauthexample/target/%s"
                             :main ^:skip-aot oauthexample.core}]})
