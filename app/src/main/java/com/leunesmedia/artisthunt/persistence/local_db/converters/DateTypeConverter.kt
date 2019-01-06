package com.leunesmedia.artisthunt.persistence.local_db.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Room Type Converter Class to Convert Date Objects
 */
class DateTypeConverter {
    /**
     * Returns Room Long to Date
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }
    /**
     * Returns Date to Room Long
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) {
            null
        } else {
            date.time
        }
    }
}