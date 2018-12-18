package com.leunesmedia.artisthunt.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object Model {
    @Entity(tableName = "user_table")
    data class User(
        var _id: String? = null,
        var firstname: String? = null,
        var lastname: String? = null,
        var email: String? = null,
        var token: String?,
        var profile_image_filename: String? = null
    ) {
        constructor() : this(
            null,
            null,
            null,
            null,
            null,
            null
        )
        @PrimaryKey(autoGenerate = true)
        var db_id: Int = 0
    }
}