package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.leunesmedia.artisthunt.domain.InjectedViewModel
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.API.UserApi
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class UserViewModel: InjectedViewModel() {
    var rawUser = MutableLiveData<Model.User>()
    var dbUser: LiveData<Model.User> = userRepo.user

    @Inject
    lateinit var userApi: UserApi

    @Inject
    lateinit var userRepo: UserRespository

    private lateinit var subscription: Disposable

}