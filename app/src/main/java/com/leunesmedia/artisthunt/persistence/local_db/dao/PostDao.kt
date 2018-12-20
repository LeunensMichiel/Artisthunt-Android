package com.leunesmedia.artisthunt.persistence.local_db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.leunesmedia.artisthunt.domain.Model

@Dao
interface PostDao
{
    @Query("SELECT * from post_table WHERE _id = :postID ORDER BY _id LIMIT 1")
    fun getPost(postID: String): LiveData<Model.Post>

    @Query("SELECT * from post_table WHERE user_id = :userID ORDER BY _id")
    fun getUserPosts(userID: String): LiveData<Model.Post>

    @Query("SELECT * from post_table ORDER BY _id ASC")
    fun getAllPosts(): LiveData<List<Model.Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Model.Post)

    @Query("DELETE FROM post_table")
    fun nukePosts()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePost(post: Model.Post)
}