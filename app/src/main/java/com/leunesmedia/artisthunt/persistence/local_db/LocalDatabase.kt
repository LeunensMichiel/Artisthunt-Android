package com.leunesmedia.artisthunt.persistence.local_db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.converters.DateTypeConverter
import com.leunesmedia.artisthunt.persistence.local_db.converters.StringArrayListConverter
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao

@Database(entities = [Model.User::class, Model.Post::class], version = 3)
@TypeConverters(StringArrayListConverter::class, DateTypeConverter::class)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        const val DATABASE_NAME = "artisthuntdb"

        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}