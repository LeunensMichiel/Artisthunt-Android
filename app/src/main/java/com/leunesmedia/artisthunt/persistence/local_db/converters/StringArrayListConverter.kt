package com.leunesmedia.artisthunt.persistence.local_db.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
/**
 * TypeConverter so an ArrayList can be inserted into Room
 */
class StringArrayListConverter {
    /**
     * Returns Arraylist of String
     */
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.getType()
        return Gson().fromJson(value, listType)
    }
    /**
     * Returns String
     */
    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}