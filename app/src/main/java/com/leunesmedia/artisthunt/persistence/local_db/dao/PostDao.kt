package com.leunesmedia.artisthunt.persistence.local_db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.leunesmedia.artisthunt.domain.Model

/**
 * RoomDao for Posts
 */
@Dao
interface PostDao
{
    /**
     * Returns a specific Post filtering on (postID)
     */
    @Query("SELECT * from post_table WHERE _id == :postID ORDER BY _id LIMIT 1")
    fun getPost(postID: String): LiveData<Model.Post>
    /**
     * Returns All the Posts of a user filtering on (user_id)
     */
    @Query("SELECT * from post_table where user_id in (:user_id) ORDER BY date desc")
    fun getUserPosts(user_id: String): List<Model.Post>
    /**
     * Returns All Posts
     */
    @Query("SELECT * from post_table ORDER BY date desc")
    fun getAllPosts(): LiveData<List<Model.Post>>

    /**
     * Inserts A post
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Model.Post)
    /**
     * Deletes All Posts
     */
    @Query("DELETE FROM post_table")
    fun nukePosts()
    /**
     * Updates a post
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePost(post: Model.Post)
}