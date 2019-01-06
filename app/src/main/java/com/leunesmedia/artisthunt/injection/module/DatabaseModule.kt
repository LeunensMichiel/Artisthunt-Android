package com.leunesmedia.artisthunt.injection.module

import android.app.Application
import android.content.Context
import com.leunesmedia.artisthunt.persistence.LocalDatabase
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao
import com.leunesmedia.artisthunt.persistence.local_db.repository.PostRepository
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Room Database Module Class Using Dagger 2.
 * (application) is Given and Dagger provides the Repositories, Dao's and the Room Database
 */
@Module
class DatabaseModule(private val application: Application) {

    /**
     * Provides userRepository with the (userDao)
     */
    @Provides
    @Singleton
    internal fun provideUserRepository(userDao: UserDao): UserRespository {
        return UserRespository(userDao)
    }
    /**
     * Provides postRepository with the (postDao)
     */
    @Provides
    @Singleton
    internal fun providePostRepository(postDao: PostDao): PostRepository {
        return PostRepository(postDao)
    }
    /**
     * Provides UserDao with the (database)
     */
    @Provides
    @Singleton
    internal fun provideUserDao(database: LocalDatabase): UserDao {
        return database.userDao()
    }
    /**
     * Provides PostDao with the (database)
     */
    @Provides
    @Singleton
    internal  fun providePostDao(database: LocalDatabase): PostDao {
        return database.postDao()
    }

    /**
     * Provides Database with the (context)
     */
    @Provides
    @Singleton
    internal fun provideDatabase(context: Context): LocalDatabase {
        return LocalDatabase.getDatabase(context)
    }

    /**
     * Provides context of the Application
     */
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }
}