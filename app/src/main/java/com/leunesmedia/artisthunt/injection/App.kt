package com.leunesmedia.artisthunt.injection

import android.app.Application
import com.leunesmedia.artisthunt.injection.component.DaggerViewModelComponent
import com.leunesmedia.artisthunt.injection.component.ViewModelComponent
import com.leunesmedia.artisthunt.injection.module.DatabaseModule
import com.leunesmedia.artisthunt.injection.module.NetworkModule

/**
 * This is the Application, user for Dagger Injection
 * Contains a Companion Object: component
 */
class App : Application() {

    companion object {
        lateinit var component: ViewModelComponent
    }

    /**
     * When it is created, a DaggerViewModelComponent is created with Network and DatabaseModule and saved in the component Companion Object
     */
    override fun onCreate() {
        super.onCreate()
        component = DaggerViewModelComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .networkModule(NetworkModule())
            .build()
    }
}