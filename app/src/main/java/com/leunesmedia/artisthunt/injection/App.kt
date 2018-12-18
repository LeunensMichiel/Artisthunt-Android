package com.leunesmedia.artisthunt.injection

import android.app.Application
import com.leunesmedia.artisthunt.injection.component.DaggerViewModelComponent
import com.leunesmedia.artisthunt.injection.component.ViewModelComponent
import com.leunesmedia.artisthunt.injection.module.DatabaseModule
import com.leunesmedia.artisthunt.injection.module.NetworkModule

class App : Application() {

    companion object {
        lateinit var component: ViewModelComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerViewModelComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .networkModule(NetworkModule())
            .build()
    }
}