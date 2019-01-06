package com.leunesmedia.artisthunt.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

/**
 * Contains all the Models for the app
 */
object Model {
    /**
     * Model to retrieve a message from the server
     */
    data class Message(
        var data: String? = "none"
    )

    /**
     * Room Model for the User
     */
    @Entity(tableName = "user_table")
    data class User(
        var _id: String? = null,
        var firstname: String? = null,
        var lastname: String? = null,
        var email: String? = null,
        var token: String? = null,
        var profile_image_filename: String? = null
        ) {
        @PrimaryKey(autoGenerate = true)
        var db_id: Int = 0
    }

    /**
     * Model to do a login Post API call to the server
     */
    data class Login(
        val email: String,
        val password: String
    )
    /**
     * Model to do a register Post API call to the server
     */
    data class Register(
        val email: String,
        val password: String,
        val firstname: String,
        val lastname: String
    )

    /**
     * Room model for a post
     */
    @Entity(tableName = "post_table")
    data class Post(
        var _id: String? = null,
        var title: String? = null,
        var description: String? = null,
        var type: String? = null,
        var user_id: String? = null,
        var post_image_filename: String? = null,
        var post_audio_filename: String? = null,
        var date: Date? = null,
        var likers: MutableList<String> = ArrayList()
        ) {
        @PrimaryKey(autoGenerate = true)
        var db_id: Int = 0
    }

    /**
     * Model to do a Put API call to the server so a post can get liked and unliked
     */
    data class updateLiker(
        var liker : String
    )
}