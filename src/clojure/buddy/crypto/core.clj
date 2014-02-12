;; Copyright 2013 Andrey Antukh <niwi@niwi.be>
;;
;; Licensed under the Apache License, Version 2.0 (the "License")
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns buddy.crypto.core
  "Basic crypto primitives that used for more high
  level abstractions."
  (:require [clojure.string :refer [trim]]
            [buddy.codecs :refer :all]
            [buddy.crypto.keys :as keys])
  (:import (org.apache.commons.codec.binary Base64 Hex)
           (javax.crypto Mac)
           (javax.crypto.spec SecretKeySpec)
           (java.security MessageDigest SecureRandom)))

(defn digest
  "Generic function for create cryptographic hash. Given an algorithm
name and many parts as byte array. Returns a computed hash as byte array.
This function hides java api to `java.security.MessageDigest`"
  [algorithm & parts]
  (let [md (MessageDigest/getInstance algorithm)]
    (doseq [part parts]
      (.update md part))
    (.digest md)))

(def ^{:doc "Create sha256 secure hash."}
  sha256 (partial digest "SHA-256"))

(def ^{:doc "Create sha384 secure hash."}
  sha384 (partial digest "SHA-384"))

(def ^{:doc "Create sha512 secure hash."}
  sha512 (partial digest "SHA-512"))

(def ^{:doc "Create sha1 secure hash."}
  sha1 (partial digest "SHA-1"))

(def ^{:doc "Create md5 secure hash."}
  md5 (partial digest "MD5"))

(defn hmac
  "Generic function that implement salted variant
of keyed-hash message authentication code (hmac)."
  [algorithm value pkey & [{:keys [salt] :or {salt ""}}]]
  (let [salt  (cond
                (bytes? salt) salt
                (string? salt) (str->bytes salt)
                :else (throw (IllegalArgumentException. "invalid salt type")))
        sks   (SecretKeySpec. (sha512 (keys/key->bytes pkey) salt) "HmacSHA256")
        mac   (doto (Mac/getInstance "HmacSHA256")
                (.init sks))]
    (.doFinal mac (str->bytes value))))

(def ^{:doc "Function that implements the HMAC algorithm with SHA256 digest mode."}
  hmac-sha256 (partial hmac "HmacSHA256"))

(def ^{:doc "Function that implements the HMAC algorithm with SHA384 digest mode."}
  hmac-sha384 (partial hmac "HmacSHA384"))

(def ^{:doc "Function that implements the HMAC algorithm with SHA512 digest mode."}
  hmac-sha512 (partial hmac "HmacSHA512"))

(defn random-bytes
  "Generate a byte array of random bytes using
  secure random generator."
  [^long s]
  (let [data  (byte-array s)
        sr    (SecureRandom/getInstance "SHA1PRNG")]
    (.nextBytes sr data)
    data))

(defn random-salt
  "Generates a random salt using a secure
  random number generator."
  (^String [] (random-salt 8))
  (^String [^long s]
   (let [rbytes (random-bytes (long (/ s 2)))]
     (bytes->hex rbytes))))

(defn timestamp
  "Get current timestamp."
  []
  (quot (System/currentTimeMillis) 1000))