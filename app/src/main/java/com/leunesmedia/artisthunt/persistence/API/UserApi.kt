package com.leunesmedia.artisthunt.persistence.API

import com.leunesmedia.artisthunt.domain.Model
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApi {
    @POST("/API/users/login")
    fun login(@Body response: Model.Login): Observable<Model.User>

    @POST("/API/users/register")
    fun register(@Body response: Model.Register): Observable<Model.User>

    @Multipart
    @PUT("/API/users/user/{id}/updateProfileImage")
    fun updateUserProfilePicture(@Path("id") id: String, @Part file: MultipartBody.Part) : Observable<Model.Message>
}