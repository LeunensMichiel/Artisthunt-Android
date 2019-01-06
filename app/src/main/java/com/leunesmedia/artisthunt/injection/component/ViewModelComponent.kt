package com.leunesmedia.artisthunt.injection.component

import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.injection.App
import com.leunesmedia.artisthunt.injection.module.DatabaseModule
import com.leunesmedia.artisthunt.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger 2 builder Interface so the NetworkModule and DatabaseModule can get injected into one of the child ViewModels
 */
@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface ViewModelComponent {
    /**
     * Injects (app)
     */
    fun inject(app: App)
    /**
     * Injects (userViewModel)
     */
    fun inject(userViewModel: UserViewModel)
    /**
     * Injects (postViewModel)
     */
    fun inject(postViewModel: PostViewModel)

}