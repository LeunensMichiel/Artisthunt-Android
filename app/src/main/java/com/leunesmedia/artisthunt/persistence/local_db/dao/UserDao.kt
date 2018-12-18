package com.leunesmedia.artisthunt.persistence.local_db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.leunesmedia.artisthunt.domain.Model

@Dao
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