package com.leunesmedia.artisthunt.persistence.local_db.converters

import android.arch.persistence.room.TypeConverter
import java.util.*


class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) {
            null
        } else {
            date.time
        }
    }
}