package com.leunesmedia.artisthunt.injection.component

import com.leunesmedia.artisthunt.domain.viewmodel.PostViewModel
import com.leunesmedia.artisthunt.domain.viewmodel.UserViewModel
import com.leunesmedia.artisthunt.injection.App
import com.leunesmedia.artisthunt.injection.module.DatabaseModule
import com.leunesmedia.artisthunt.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface ViewModelComponent {
    fun inject(app: App)
    fun inject(userViewModel: UserViewModel)
    fun inject(postViewModel: PostViewModel)

}