(ns caves-of-clojure.world)

(def world-size [160 50])

(defrecord World [tiles])

(defrecord Tile [kind glyph color])

(def tiles
  {:floor (new Tile :floor "." :white)
   :wall (new Tile :wall "#" :white)
   :bound (new Tile :bound "X" :black)})

(defn get-tile [given-tiles x y]
  (get-in given-tiles [y x] (get tiles :bound)))

(get-tile {:bound "foo", :key-y {}} :key-x :key-y)

;ruby equiv: tiles[x][y] || tiles[:bound]

(defn random-tiles []
  (let [[cols rows] world-size]
    ))



