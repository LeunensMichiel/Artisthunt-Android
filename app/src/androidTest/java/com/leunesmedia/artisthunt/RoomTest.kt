package com.leunesmedia.artisthunt

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.LocalDatabase
import com.leunesmedia.artisthunt.persistence.local_db.dao.PostDao
import com.leunesmedia.artisthunt.persistence.local_db.dao.UserDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var userDao: UserDao
    private lateinit var postDao: PostDao
    private lateinit var db: LocalDatabase
    private var user = Model.User(
        firstname = "Tom"
    )
    private var post = Model.Post(
        title = "Title",
        _id = "1234",
        user_id = "1"
    )

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            LocalDatabase::class.java
        ).build()
        userDao = db.userDao()
        postDao = db.postDao()
        user.firstname = "Tom"
        post.title = "Title"
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        userDao.nukeUsers()
        postDao.nukePosts()
        db.close()
    }

    @Test
    fun insertingUserSavesUser() {
        userDao.insert(user)

        val dbUser = userDao.getUser()
        assert(dbUser.value?.firstname == user.firstname)
    }

    @Test
    fun updatingUserUpdatesUser() {
        userDao.insert(user)

        user.firstname = "Sven"
        userDao.updateUser(user)

        val updatedUser = userDao.getUser()
        assert(updatedUser.value?.db_id == user.db_id)
    }

    @Test
    fun nukeUsersDeletesUsersFromDatabase() {
        userDao.insert(user)

        userDao.nukeUsers()
        val user = userDao.getUser()

        assert(user.value == null)
    }

    @Test
    fun insertingPostWillSavePost() {
        postDao.insert(post)
        assert(postDao.getPost("1234").value != null)
    }

    @Test
    fun returningMultiplePostWillReturnAllPosts() {
        val post2 = post.copy(title = "Title2", user_id = "1", _id = "2345")
        val post3 = post.copy(title = "Title3", user_id = "2", _id = "7890")

        postDao.insert(post)
        postDao.insert(post2)
        postDao.insert(post3)

        assert(postDao.getAllPosts().value?.size == 3)
    }

    @Test
    fun returningUserPostsWillReturnUserPosts() {
        val post2 = post.copy(title = "Title2", user_id = "1", _id = "2345")
        val post3 = post.copy(title = "Title3", user_id = "2", _id = "7890")

        postDao.insert(post)
        postDao.insert(post2)
        postDao.insert(post3)

        assert(postDao.getAllPosts().value?.size == 2)
    }

}