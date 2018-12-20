package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import org.jetbrains.anko.doAsync

class PostRepository(private val dao: PostDao) {
    var allPosts: LiveData<List<Model.Post>> = dao.getAllPosts()

    @WorkerThread
    fun getSinglePost(postid: String) {
        doAsync {
            dao.getPost(postid)
        }
    }
    @WorkerThread
    fun getUserPosts(userid: String) {
        doAsync {
            dao.getUserPosts(userid)
        }
    }

    @WorkerThread
    fun insert(post: Model.Post) {
        doAsync {
            dao.insert(post)
        }
    }

    @WorkerThread
    fun nukePosts() {
        doAsync {
            dao.nukePosts()
        }
    }

    @WorkerThread
    fun updatePost(post: Model.Post) {
        doAsync {
            dao.updatePost(post)
        }
    }

}