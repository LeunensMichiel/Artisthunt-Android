package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import org.jetbrains.anko.doAsync

class PostRepository(private val dao: PostDao) {
    var post: LiveData<Model.Post> = dao.getPost()

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