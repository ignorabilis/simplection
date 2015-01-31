(ns simplection.preview.helpers)

(defn svg-quotes-helper
  "In the REPL all double quotes are escaped. Therefore for quick copy-paste testing purposes use this helper to convert double quotes to single quotes"
  [incoming-html]
  (clojure.string/replace incoming-html "\"" "'"))