package com.duke.orca.android.kotlin.calendarlockscreen.calendar

import android.provider.CalendarContract

internal object Events {
    val projections: Array<String> = arrayOf(
        CalendarContract.Events._ID,
        CalendarContract.Events.DTSTART
    )

    const val selection = "MAX(${CalendarContract.Events._ID})"

    object Index {
        const val ID = 0
        const val DTSTART = 1
    }
}

internal object Instances {
    val projections: Array<String> = arrayOf(
        CalendarContract.Instances._ID,
        CalendarContract.Instances.ALL_DAY,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Instances.CALENDAR_COLOR,
        CalendarContract.Instances.CALENDAR_ID,
        CalendarContract.Instances.END,
        CalendarContract.Instances.END_DAY,
        CalendarContract.Instances.EVENT_COLOR,
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.START_DAY,
        CalendarContract.Instances.TITLE
    )

    const val selection = CalendarContract.Calendars.VISIBLE + "=?"
    const val sortOrder = CalendarContract.Instances.START_DAY + " ASC, " +
            CalendarContract.Instances.END_DAY + " DESC, " +
            CalendarContract.Instances.TITLE + " ASC"

    val selectionArgs = arrayOf("1")

    object Index {
        const val ID = 0
        const val ALL_DAY = 1
        const val BEGIN = 2
        const val CALENDAR_COLOR = 3
        const val CALENDAR_ID = 4
        const val END = 5
        const val END_DAY = 6
        const val EVENT_COLOR = 7
        const val EVENT_ID = 8
        const val START_DAY = 9
        const val TITLE = 10
    }
}