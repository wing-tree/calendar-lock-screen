package com.duke.orca.android.kotlin.calendarlockscreen.calendar.repository

import android.Manifest.permission.READ_CALENDAR
import android.Manifest.permission.WRITE_CALENDAR
import android.content.ContentUris
import android.content.Context
import android.provider.CalendarContract
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.DAYS_PER_WEEK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.Instances
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.WEEKS_PER_MONTH
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.model.*
import com.duke.orca.android.kotlin.calendarlockscreen.permission.PermissionChecker
import java.util.*
import javax.inject.Inject

class CalendarRepositoryImpl(private val applicationContext: Context) : CalendarRepository {
    private val contentResolver = applicationContext.contentResolver

    override suspend fun getInstances(begin: Long, end: Long): List<Instance> {
        if (permissionsGranted().not()) return emptyList()

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()

        ContentUris.appendId(builder, begin)
        ContentUris.appendId(builder, end)

        val cursor = contentResolver.query(
            builder.build(),
            Instances.projections,
            Instances.selection,
            Instances.selectionArgs,
            Instances.sortOrder
        ) ?: return emptyList()

        val list = arrayListOf<Instance>()

        while (cursor.moveToNext()) {
            val instance = Instance.from(cursor)

            if (instance.begin < begin && instance.duration < 1 && instance.isAllDay) continue

            list.add(Instance.from(cursor))
        }

        return list
    }

    override suspend fun getWeekOfMonthToInstances(year: Int, month: Int): WeekOfMonthToInstances {
        val weekOfMonthToInstances = WeekOfMonthToInstances()

        repeat(WEEKS_PER_MONTH) {
            weekOfMonthToInstances.set(it, getInstancesOfWeekOfMonth(year, month, it.inc()))
        }

        return weekOfMonthToInstances
    }

    private suspend fun getInstancesOfWeekOfMonth(year: Int, month: Int, weekOfMonth: Int): List<Instance> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.WEEK_OF_MONTH, weekOfMonth)
            set(Calendar.DAY_OF_WEEK, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val begin = calendar.timeInMillis
        val end = Calendar.getInstance().apply {
            timeInMillis = begin
            add(Calendar.DATE, DAYS_PER_WEEK.dec())
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return getInstances(begin, end)
    }

    private fun permissionsGranted(): Boolean {
        return PermissionChecker.checkPermissions(applicationContext, listOf(READ_CALENDAR, WRITE_CALENDAR))
    }
}