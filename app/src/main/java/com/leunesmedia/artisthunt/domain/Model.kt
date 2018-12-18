package com.leunesmedia.artisthunt.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object Model {
    data class Message(
        var data: String? = "none"
    )

    @Entity(tableName = "user_table")
    data class User(
        var _id: String? = null,
        var firstname: String? = null,
        var lastname: String? = null,
        var email: String? = null,
        var token: String?,
        var profile_image_filename: String? = null
    ) {
        @PrimaryKey(autoGenerate = true)
        var db_id: Int = 0
    }

    data class Login(
        val email: String,
        val password: String
    )

    data class Register(
        val email: String,
        val password: String,
        val firstname: String,
        val lastname: String
    )
}