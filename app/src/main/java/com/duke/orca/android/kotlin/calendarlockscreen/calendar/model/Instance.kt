package com.duke.orca.android.kotlin.calendarlockscreen.calendar.model

import android.database.Cursor
import android.provider.CalendarContract
import androidx.annotation.ColorInt
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.Instances
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository.CalendarRepositoryImpl

data class Instance(
    val begin: Long,
    @ColorInt
    val calendarColor: Int,
    val calendarId: Long,
    val duration: Int,
    val end: Long,
    val endDay: Int,
    @ColorInt
    val eventColor: Int,
    val eventId: Long,
    val id: Long,
    val startDay: Int,
    val title: String,
    var isAllDay: Boolean = false,
    var isTranslucent: Boolean = false,
    var isVisible: Boolean = true,
    var spanCount: Int = 1
) {
    fun isFillBackgroundColor() = when {
        isAllDay -> true
        spanCount > 1 -> true
        else -> false
    }

    companion object {
        fun from(cursor: Cursor) : Instance {
            val allDay = cursor.getInt(Instances.Index.ALL_DAY)
            val begin = cursor.getLong(Instances.Index.BEGIN)
            val calendarColor = cursor.getInt(Instances.Index.CALENDAR_COLOR)
            val calendarId = cursor.getLong(Instances.Index.CALENDAR_ID)
            val end = cursor.getLong(Instances.Index.END)
            val endDay = cursor.getInt(Instances.Index.END_DAY)
            val eventColor = cursor.getInt(Instances.Index.EVENT_COLOR)
            val eventId = cursor.getLong(Instances.Index.EVENT_ID)
            val id = cursor.getLong(Instances.Index.ID)
            val startDay = cursor.getInt(Instances.Index.START_DAY)
            val title = cursor.getString(Instances.Index.TITLE)

            return Instance(
                begin = begin,
                calendarColor = calendarColor,
                calendarId = calendarId,
                end = end,
                endDay = endDay,
                eventColor = eventColor,
                eventId = eventId,
                duration = endDay - startDay,
                id = id,
                isAllDay = allDay == 1,
                startDay = startDay,
                title = title,
            )
        }
    }
}