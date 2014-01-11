(ns blog.controllers.admin.posts
  (:require
    [clostache.parser :as clostache]
    [blog.models.posts :as posts-model]))

(defn read-template [template-name]
  (slurp (clojure.java.io/resource
    (str "views/admin/posts/" template-name ".mustache"))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

(defn index [] 
  (render-template "index" {:posts (posts-model/all)}))

(defn show [id]
  (render-template "show" {:post (posts-model/get id)}))

(defn edit [id]
  (render-template "edit" {:post (posts-model/get id)}))

(defn new []
  (render-template "new" {}))