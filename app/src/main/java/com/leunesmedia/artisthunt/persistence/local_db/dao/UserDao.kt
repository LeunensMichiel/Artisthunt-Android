package com.leunesmedia.artisthunt.persistence.local_db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.leunesmedia.artisthunt.domain.Model

/**
 * RoomDAO for User
 */
@Dao
interface UserDao
{
    /**
     * Returns current User
     */
    @Query("SELECT * from user_table ORDER BY _id LIMIT 1")
    fun getUser(): LiveData<Model.User>
    /**
     * Inserts Current User into the DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user:Model.User)
    /**
     * Deletes User From Database
     */
    @Query("DELETE FROM user_table")
    fun nukeUsers()
    /**
     * Updates User in database
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: Model.User)
}