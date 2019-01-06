package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import org.jetbrains.anko.doAsync

/**
 * Repository Class for Posts
 */
class PostRepository(private val dao: PostDao) {
    /**
     * Returns all Posts from the dao
     */
    var allPosts: LiveData<List<Model.Post>> = dao.getAllPosts()

    /**
     * Returns single post from the Dao with (postid)
     */
    fun getSinglePost(postid: String) : LiveData<Model.Post> {
           return dao.getPost(postid)
    }
    /**
     * Returns all posts from the Dao with (userid)
     */
    fun getUserPosts(userid: String) : List<Model.Post> {
           return dao.getUserPosts(userid)
    }
    /**
     * Async Method to Insert Post into DAO
     */
    @WorkerThread
    fun insert(post: Model.Post) {
        doAsync {
            dao.insert(post)
        }
    }
    /**
     * Async Method to Delete Post in DAO
     */
    @WorkerThread
    fun nukePosts() {
        doAsync {
            dao.nukePosts()
        }
    }
    /**
     * Async method to modify Post in DAO
     */
    @WorkerThread
    fun updatePost(post: Model.Post) {
        doAsync {
            dao.updatePost(post)
        }
    }

}