package com.leunesmedia.artisthunt.persistence.local_db.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * TypeConverter so a List can be inserted into Room
 */
class StringListConverter {
    /**
     * Returns List of String
     */
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {

        }.type
        return Gson().fromJson(value, listType)
    }
    /**
     * Returns String Data
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}