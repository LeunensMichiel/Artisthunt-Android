package com.leunesmedia.artisthunt.injection.module

import android.app.Application
import android.content.Context
import com.leunesmedia.artisthunt.persistence.local_db.LocalDatabase
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao
import com.leunesmedia.artisthunt.persistence.local_db.repository.PostRepository
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideUserRepository(userDao: UserDao): UserRespository {
        return UserRespository(userDao)
    }

    @Provides
    @Singleton
    internal fun providePostRepository(postDao: PostDao): PostRepository {
        return PostRepository(postDao)
    }

    @Provides
    @Singleton
    internal fun provideUserDao(database: LocalDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    internal  fun providePostDao(database: LocalDatabase): PostDao {
        return database.postDao()
    }

    @Provides
    @Singleton
    internal fun provideDatabase(context: Context): LocalDatabase {
        return LocalDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }
}