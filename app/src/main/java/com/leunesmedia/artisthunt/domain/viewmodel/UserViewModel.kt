package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
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

/**
 * This viewmodel class contains all the networking and repository logic for a user and injects UserRepository and UserApi
 * It contains a uiMessage and the userPosts of the current User which the UI can observe
 */
class UserViewModel: InjectedViewModel() {

    var uiMessage = MutableLiveData<Model.Message>()

    @Inject
    lateinit var userApi: UserApi

    @Inject
    lateinit var userRepo: UserRespository

    private lateinit var subscription: Disposable

    /**
     * Tries to log the user in and sends API call to the Server with (loginDetails).
     * On succes the method onRetrieveUserSucces is called
     * Uimessage is updated accordingly
     */
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

    /**
     * Tries to register the user and sends API call to the Server with (registerDetails). On succes the method
     * onRetrieveUserSucces is called
     * Uimessage is updated accordingly
     */
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

    /**
     * Tries to change the user's profile picture and sends the (file) to the server
     * On Succes, the user gets updated in the RoomDatabase
     */
    fun changeProfilePicture(file: File) {
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

    /**
     * The (user) is inserted into the Room Database
     */
    private fun onRetrieveUserSucces(user: Model.User?) {
        userRepo.insert(user!!)
    }

}