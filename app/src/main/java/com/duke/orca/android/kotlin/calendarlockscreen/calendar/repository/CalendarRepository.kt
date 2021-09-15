package com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository

import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Event
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.Instance
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.WeekOfMonthToInstances

interface CalendarRepository {
    suspend fun getInstances(begin: Long, end: Long): List<Instance>
    suspend fun getLastEvent(): Event?
    suspend fun getWeekOfMonthToInstances(year: Int, month: Int): WeekOfMonthToInstances
}