package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao
import org.jetbrains.anko.doAsync

class UserRespository(private val dao: UserDao) {
    var user: LiveData<Model.User> = dao.getUser()

    @WorkerThread
    fun insert(user: Model.User) {
        doAsync {
            dao.insert(user)
        }
    }

    @WorkerThread
    fun nukeUsers() {
        doAsync {
            dao.nukeUsers()
        }
    }

    @WorkerThread
    fun updateUser(user: Model.User) {
        doAsync {
            dao.updateUser(user)
        }
    }
}