package com.leunesmedia.artisthunt.persistence.local_db.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao
import org.jetbrains.anko.doAsync
/**
 * Repository Class for Posts
 */
class UserRespository(private val dao: UserDao) {
    /**
     * Returns current user from the dao
     */
    var user: LiveData<Model.User> = dao.getUser()
    /**
     * Async Method to Insert User into DAO
     */
    @WorkerThread
    fun insert(user: Model.User) {
        doAsync {
            dao.insert(user)
        }
    }
    /**
     * Async Method to Delete User into DAO
     */
    @WorkerThread
    fun nukeUsers() {
        doAsync {
            dao.nukeUsers()
        }
    }
    /**
     * Async Method to modify User into DAO
     */
    @WorkerThread
    fun updateUser(user: Model.User) {
        doAsync {
            dao.updateUser(user)
        }
    }
}