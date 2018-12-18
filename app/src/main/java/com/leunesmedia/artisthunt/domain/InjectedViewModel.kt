package com.leunesmedia.artisthunt.domain

import android.arch.lifecycle.ViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.injection.App

abstract class InjectedViewModel: ViewModel() {

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is UserViewModel -> App.component.inject(this)
        }
    }
}