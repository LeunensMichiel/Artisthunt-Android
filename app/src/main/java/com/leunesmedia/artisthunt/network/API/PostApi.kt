package com.leunesmedia.artisthunt.network.API

import com.leunesmedia.artisthunt.domain.Model
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Retrofit Interface for working with Post Objects over the Network
 */
interface PostApi {
    /**
     * Retrieves all Post Objects
     */
    @GET("/API/post/post")
    fun getPosts(): Observable<Array<Model.Post>>
    /**
     * Retrieves all User's Post Objects
     */
    @GET("/API/post/post/{user_id}")
    fun getUserPosts(@Path("user_id") user_id: String): Observable<Array<Model.Post>>

    /**
     * Adds A Post Object with just Text
     */
    @POST("/API/post/post")
    fun addPost(@Body post: Model.Post): Observable<Model.Post>

    /**
     * Adds a Post Object containing an Image and uses Multipart Form Data
     */
    @Multipart
    @PUT("/API/post/post/image")
    fun addImagePost(@Part("post") post: Model.Post, @Part file: MultipartBody.Part): Observable<Model.Post>

    /**
     * Updates the likes of a Post
     */
    @POST("/API/post/post/{post_id}/updateLikers")
    fun updatePostLikers(@Path("post_id") post_id: String, @Body post: Model.updateLiker): Observable<Model.updateLiker>

}