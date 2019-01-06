package com.leunesmedia.artisthunt.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

/**
 * Retrofit Adapter to Parse Date Objects to a Json String and Vice Verca
 */
class DateAdapter {
    /**
     * Turns Date into String
     */
    @ToJson
    fun toJson(date: Date) : String {
        return FORMATTER.format(date)
    }

    /**
     * Turns String into Date
     */
    @FromJson
    fun fromJson(date: String): Date {
        return FORMATTER.parse(date)
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("dd-MM-yyyy  HH:mm")
    }
}