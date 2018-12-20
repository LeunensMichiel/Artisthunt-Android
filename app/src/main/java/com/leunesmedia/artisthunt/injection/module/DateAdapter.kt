package com.leunesmedia.artisthunt.injection.module

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {
    @ToJson
    fun toJson(date: Date) : String {
        return FORMATTER.format(date)
    }

    @FromJson
    fun fromJson(date: String): Date {
        return FORMATTER.parse(date)
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("dd-MM-yyyy  hh:mm")
    }
}