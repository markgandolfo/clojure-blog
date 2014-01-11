(ns blog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.basic-authentication :refer :all]
            [ring.util.response :as resp]
            [blog.models.posts :as posts-model]
            [blog.controllers.posts :as posts-controller]
            [blog.controllers.admin.posts :as admin-posts-controller]))

(defn authenticated? [name pass]
  (and (= name "user")
       (= pass "pass")))

(defroutes public-routes
  (GET "/" [] (posts-controller/index))
  (route/resources "/" ))

(defroutes protected-routes
  (GET "/admin" [] (admin-posts-controller/index))
  (GET "/admin" [id] (admin-posts-controller/show id))
  (GET "/admin/posts/new" [] (admin-posts-controller/new))
  (POST "/admin/posts/create" [& params]
    (do (posts-model/create params)
        (resp/redirect "/admin")))
  (GET "/admin/posts/:id/edit" [id] (admin-posts-controller/edit id))
  (POST "/admin/posts/:id/save" [& params]
    (do (posts-model/save (:id params) params)
        (resp/redirect "/admin")))
  (GET "/admin/posts/:id/delete" [id]
    (do (posts-model/delete id)
      (resp/redirect "/admin"))))

(defroutes app-routes
  public-routes
  (wrap-basic-authentication protected-routes authenticated?)
  (route/not-found "404 Not Found"))

(def app
  (handler/site app-routes))