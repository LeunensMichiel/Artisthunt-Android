package com.leunesmedia.artisthunt.network.API

import com.leunesmedia.artisthunt.domain.Model
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
/**
 * Retrofit Interface for working with User Objects over the Network
 */
interface UserApi {
    /**
     * Logs the User in on the Server
     */
    @POST("/API/users/login")
    fun login(@Body response: Model.Login): Observable<Model.User>

    /**
     * Registers a User on the Server
     */
    @POST("/API/users/register")
    fun register(@Body response: Model.Register): Observable<Model.User>

    /**
     * Updates User's Profile Picture
     */
    @Multipart
    @PUT("/API/users/user/{id}/updateProfileImage")
    fun updateUserProfilePicture(@Path("id") id: String, @Part file: MultipartBody.Part) : Observable<Model.Message>
}