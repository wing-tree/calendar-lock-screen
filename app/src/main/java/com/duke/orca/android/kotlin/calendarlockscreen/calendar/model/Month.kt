package com.duke.orca.android.kotlin.calendarlockscreen.calendar.model

import android.icu.util.Calendar
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.DAYS_PER_WEEK
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.VISIBLE_INSTANCE_COUNT
import com.duke.orca.android.kotlin.calendarlockscreen.calendar.WEEKS_PER_MONTH

class Month(val year: Int, val month: Int) {
    private val weeks = arrayListOf<Week>()
    private val firstJulianDayOfMonth: Int//? by lazy { weeks[0].getFirstDay() }

    init {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.WEEK_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        repeat (WEEKS_PER_MONTH) {
            val week = Week()

            weeks.add(week)

            repeat(DAYS_PER_WEEK) {
                val date = calendar.get(Calendar.DATE)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val julianDay = calendar.get(Calendar.JULIAN_DAY)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)

                calendar.add(Calendar.DATE, 1)
                week.days[julianDay] = Day(
                    date = date,
                    dayOfWeek = dayOfWeek,
                    isTranslucent = month != this.month,
                    julianDay = julianDay,
                    month = month,
                    year = year,
                    nextKey = calendar.get(Calendar.JULIAN_DAY)
                )
            }
        }

        firstJulianDayOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.WEEK_OF_MONTH, 1)
            set(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.get(Calendar.JULIAN_DAY)
    }

    fun get(weekOfMonth: Int): Week {
        if (weekOfMonth in 0 until WEEKS_PER_MONTH) {
            return weeks[weekOfMonth]
        } else {
            throw IndexOutOfBoundsException()
        }
    }

    fun setWeekOfMonthToInstances(weekOfMonthToInstances: WeekOfMonthToInstances) {
        for (weekOfMonth in 0 until WEEKS_PER_MONTH) {
            for (instance in weekOfMonthToInstances.get(weekOfMonth)) {
                val week = weeks[weekOfMonth]
                val firstDayOfWeek = week.getFirstDay() ?: continue

                if (instance.startDay < firstDayOfWeek.julianDay) {
                    var j = -1

                    firstDayOfWeek.visibleInstances.apply {
                        for (i in 0 until VISIBLE_INSTANCE_COUNT) {
                            if (get(i) == null) {
                                instance.isTranslucent = firstDayOfWeek.isTranslucent

                                if (weekOfMonth == 0 && instance.endDay > firstJulianDayOfMonth)
                                    instance.isTranslucent = false

                                set(i, instance)
                                j = i
                                break
                            }
                        }
                    }

                    var nextKey = firstDayOfWeek.nextKey
                    var spanCount = 1

                    for (i in 0 until instance.duration) {
                        val nextDay = week.days[nextKey] ?: break

                        if (instance.endDay < nextDay.julianDay) break

                        ++spanCount

                        if (j in 0 until VISIBLE_INSTANCE_COUNT) {
                            nextDay.visibleInstances[j] = instance.copy().apply {
                                isVisible = false
                            }

                            nextKey = nextDay.nextKey
                        } else {
                            break
                        }
                    }

                    if (j in 0 until VISIBLE_INSTANCE_COUNT) {
                        firstDayOfWeek.visibleInstances[j]?.spanCount = spanCount
                    }
                } else {
                    val day = week.days[instance.startDay] ?: continue
                    var j = -1

                    day.visibleInstances.apply {
                        for (i in 0 until VISIBLE_INSTANCE_COUNT) {
                            if (get(i) == null) {
                                instance.isTranslucent = day.isTranslucent

                                if (weekOfMonth == 0 && instance.endDay > firstJulianDayOfMonth)
                                    instance.isTranslucent = false

                                set(i, instance)
                                j = i
                                break
                            }
                        }
                    }

                    var nextKey = day.nextKey
                    var spanCount = 1

                    for (i in 0 until instance.duration) {
                        val nextDay = week.days[nextKey] ?: break

                        if (instance.endDay < nextDay.julianDay) break

                        ++spanCount

                        if (j in 0 until VISIBLE_INSTANCE_COUNT) {
                            nextDay.visibleInstances[j] = instance.copy().apply {
                                isVisible = false
                            }

                            nextKey = nextDay.nextKey
                        } else {
                            break
                        }
                    }

                    if (j in 0 until VISIBLE_INSTANCE_COUNT) {
                        day.visibleInstances[j]?.spanCount = spanCount
                    }
                }
            }
        }
    }
}