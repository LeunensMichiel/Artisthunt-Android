package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.util.Log
import com.leunesmedia.artisthunt.domain.InjectedViewModel
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.network.API.UserApi
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserViewModel: InjectedViewModel() {

    var uiMessage = MutableLiveData<Model.Message>()

    @Inject
    lateinit var userApi: UserApi

    @Inject
    lateinit var userRepo: UserRespository

    private lateinit var subscription: Disposable

    fun login(loginDetails: Model.Login) {
        subscription = userApi.login(loginDetails)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieveUserSucces(user)
                    uiMessage.postValue(Model.Message("loginSucces"))
                },
                { error ->
                    uiMessage.postValue(Model.Message("loginError"))
                }
            )
    }

    fun register(registerDetails: Model.Register){
        subscription = userApi.register(registerDetails)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieveUserSucces(user)
                    uiMessage.postValue(Model.Message("registerSucces"))
                },
                { error ->
                    uiMessage.postValue(Model.Message("registerError"))
                }
            )
    }

    fun changeProfilePicture(file: File, bitmap: Bitmap) {
        val reqFile = RequestBody.create(
            MediaType.parse("image/jpg"),
            file
        )
        val body = MultipartBody.Part.createFormData("file", file.name + ".jpg", reqFile)
        subscription = userApi.updateUserProfilePicture(userRepo.user.value?._id!!, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    userRepo.user.value?.profile_image_filename = result.data
                    userRepo.updateUser(userRepo.user.value!!)
                    uiMessage.postValue(Model.Message("updateProfileImageSucces"))
                },
                { error ->
                    Log.d("HIHIHI", error.message)
                    uiMessage.postValue(Model.Message("updateProfileImageError"))
                }
            )

    }

    private fun onRetrieveUserSucces(user: Model.User?) {
//        var pref = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
//        pref.edit().putString("authToken", user?.token).apply()
        userRepo.insert(user!!)
    }

}