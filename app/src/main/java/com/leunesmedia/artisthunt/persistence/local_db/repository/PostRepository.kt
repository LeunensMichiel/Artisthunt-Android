package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import org.jetbrains.anko.doAsync

class PostRepository(private val dao: PostDao) {
    var post: LiveData<Model.Post> = dao.getPost()
    var posts: LiveData<Model.Post> = dao.getPosts()

    @WorkerThread
    fun insert(post: Model.Post) {
        doAsync {
            dao.insert(post)
        }
    }

    @WorkerThread
    fun nukeUsers() {
        doAsync {
            dao.nukePosts()
        }
    }

    @WorkerThread
    fun updateUser(post: Model.Post) {
        doAsync {
            dao.updatePost(post)
        }
    }

}