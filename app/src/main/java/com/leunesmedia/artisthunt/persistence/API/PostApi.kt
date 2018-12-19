package com.leunesmedia.artisthunt.persistence.API

import com.leunesmedia.artisthunt.domain.Model
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    @GET("/API/post/post/{user_id}")
    fun getPosts(@Path("user_id") user_id:String): Observable<Array<Model.Post>>

    @POST("/API/post/post")
    fun addPost(@Body post:Model.Post): Observable<Model.Post>

}