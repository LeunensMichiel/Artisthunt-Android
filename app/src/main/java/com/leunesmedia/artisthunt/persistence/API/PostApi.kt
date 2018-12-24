package com.leunesmedia.artisthunt.persistence.API

import com.leunesmedia.artisthunt.domain.Model
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface PostApi {
    @GET("/API/post/post")
    fun getPosts(): Observable<Array<Model.Post>>

    @GET("/API/post/post/{user_id}")
    fun getUserPosts(@Path("user_id") user_id: String): Observable<Array<Model.Post>>

    @POST("/API/post/post")
    fun addPost(@Body post: Model.Post): Observable<Model.Post>

    @Multipart
    @PUT("/API/post/post/image")
    fun addImagePost(@Part("post") post: Model.Post, @Part file: MultipartBody.Part): Observable<Model.Post>

}