package com.leunesmedia.artisthunt.persistence.local_db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.leunesmedia.artisthunt.domain.Model

interface UserDao
{
    @Query("SELECT * from user_table ORDER BY _id LIMIT 1")
    fun getUser(): LiveData<Model.User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user:Model.User)

    @Query("DELETE FROM user_table")
    fun nukeUsers()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: Model.User)
}